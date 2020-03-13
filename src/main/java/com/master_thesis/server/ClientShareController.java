package com.master_thesis.server;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.net.URI;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class ClientShareController {
    private static final Logger log = (Logger) LoggerFactory.getLogger(ClientShareController.class);
    private HashMap<Integer, Buffer> buffers;
    private PublicParameters publicParameters;
    private RSAThreshold secretSharing;
    private HttpAdapter httpAdapter;


    @Autowired
    public ClientShareController(PublicParameters publicParameters, RSAThreshold serverSecretSharing, HttpAdapter httpAdapter) {
        this.buffers = new HashMap<>();
        this.publicParameters = publicParameters;
        this.secretSharing = serverSecretSharing;
        this.httpAdapter = httpAdapter;
    }

    @PostMapping(value = "/client-share")
    void receiveShare(@RequestBody ClientShare clientShare) {
        log.info("Received share: {} ", clientShare.toString());
        int transformatorID = clientShare.getTransformatorID();
        buffers.putIfAbsent(transformatorID, new Buffer(publicParameters, transformatorID));
        Buffer buffer = buffers.get(transformatorID);
        buffer.putClientShare(clientShare);
        if (buffer.canCompute()) {
            new Thread(() -> performComputations(transformatorID)).start();
        }
    }

    private void performComputations(int transformatorID) {
        log.info("Computing partial information for Transformator {}", transformatorID);
        Buffer buffer = buffers.get(transformatorID);
        List<BigInteger> shares = buffer.getShares();

//        Compute all common operations
        BigInteger fieldBase = publicParameters.getFieldBase(transformatorID);
        BigInteger generator = publicParameters.getGenerator(transformatorID);
        BigInteger partialResult = secretSharing.partialEval(shares, fieldBase);
        BigInteger lastClientProof = secretSharing.lastClientProof(buffer.getNonces(), fieldBase, generator);
        List<BigInteger> clientProofs = buffer.getProofComponents();
        clientProofs.add(lastClientProof);
        PartialObject partialObject = new PartialObject(partialResult, transformatorID, publicParameters.getServerID());

//        Compute proofs for HomomorphicHash construction
        BigInteger homomorphicPartialProof = secretSharing.homomorphicPartialProof(shares, fieldBase, generator);
        partialObject.setHomomorphicPartialProof(homomorphicPartialProof);

//        Compute proofs for RSAThreshold construcion
        List<RSAProofInfo> rsaProofInfo = buffer.getRSAProofInformation(transformatorID);
        RSAProofInfo lastClient = new RSAProofInfo(rsaProofInfo.get(0), lastClientProof);
        rsaProofInfo.add(lastClient);

        ClientInfo[] clientInfos = secretSharing.rsaPartialProof(rsaProofInfo, transformatorID);
        partialObject.setClientInfos(clientInfos);

//        Send all shares
        URI uri = URI.create("http://localhost:3000/api/partials");
        buffer.remove();
        try {
            httpAdapter.sendWithTimeout(uri, partialObject, 1000);
            log.info("Sent {} to {}", partialObject, uri);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }
}
