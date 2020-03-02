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
    private HashMap<Integer, Buffer> buffers;
    private PublicParameters publicParameters;
    private ServerSecretSharing secretSharing;
    private HttpAdapter httpAdapter;
    private static final Logger log = (Logger) LoggerFactory.getLogger(ClientShareController.class);


    @Autowired
    public ClientShareController(PublicParameters publicParameters, ServerSecretSharing serverSecretSharing, HttpAdapter httpAdapter) {
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
            log.info("Computing partial information for Transformator {}", transformatorID);
            List<BigInteger> shares = buffer.getShares();
            BigInteger fieldBase = publicParameters.getFieldBase(transformatorID);
            BigInteger generator = publicParameters.getGenerator(transformatorID);
            BigInteger partialResult = secretSharing.partialEval(shares, fieldBase);

            BigInteger serverPartialProof = secretSharing.partialProof(shares, fieldBase, generator);
            BigInteger lastClientProof = secretSharing.lastClientProof(buffer.getNonces(), fieldBase, generator);

            List<BigInteger> clientProofs = buffer.getProofComponents();
            clientProofs.add(lastClientProof);

            PartialObject partialObject = new PartialObject(partialResult, serverPartialProof, clientProofs, transformatorID, publicParameters.getServerID());
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
}
