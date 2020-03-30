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
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class ClientShareController {
    private static final Logger log = (Logger) LoggerFactory.getLogger(ClientShareController.class);
    private Buffer buffer;
    private PublicParameters publicParameters;
    private HomomorphicHash secretSharingHomomorphicHash;
    private RSAThreshold secretSharingRSAThreshold;
    private HttpAdapter httpAdapter;


    @Autowired
    public ClientShareController(PublicParameters publicParameters, HomomorphicHash secretSharingHomomorphicHash, RSAThreshold secretSharingRSAThreshold, HttpAdapter httpAdapter) {
        this.buffer = new Buffer(publicParameters);
        this.publicParameters = publicParameters;
        this.secretSharingHomomorphicHash = secretSharingHomomorphicHash;
        this.secretSharingRSAThreshold = secretSharingRSAThreshold;
        this.httpAdapter = httpAdapter;
    }

    @PostMapping(value = "/client-share")
    void receiveShare(@RequestBody ClientShare clientShare) {
        log.info("Received share: {} ", clientShare.toString());
        buffer.putClientShare(clientShare);
        if (buffer.canCompute(clientShare.getSubstationID(), clientShare.getFid())) {
            new Thread(() -> performComputations(clientShare.getSubstationID(), clientShare.getFid())).start();
        }
    }

    private void performComputations(int substationID, int fid) {
        log.info("Computing partial information for Substation {} fid {} ", substationID, fid);
        Buffer.Fid fidData = buffer.getFid(substationID, fid);
        List<BigInteger> shares = fidData.getShares();

//      Compute all common operations
        BigInteger fieldBase = publicParameters.getFieldBase(substationID);
        BigInteger generator = publicParameters.getGenerator(substationID);
        BigInteger partialResult;
        PartialObject partialObject = new PartialObject(substationID, publicParameters.getServerID());


//      Compute proofs for HomomorphicHash construction
        if (shares != null) {
            BigInteger homomorphicPartialProof = secretSharingHomomorphicHash.homomorphicPartialProof(shares, fieldBase, generator);
            partialResult = secretSharingHomomorphicHash.partialEval(shares, fieldBase);
            partialObject.setHomomorphicPartialProof(homomorphicPartialProof);
            partialObject.setPartialResult(partialResult);
        }

//      Compute proofs for RSAThreshold construction
        List<RSAProofInfo> rsaProofInfo = fidData.getRSAProofInformation();
        if (shares != null && rsaProofInfo != null && rsaProofInfo.get(0).clientProof != null && rsaProofInfo.get(0).getSkShare() != null) {
            ClientInfo[] clientInfos = secretSharingRSAThreshold.rsaPartialProof(rsaProofInfo, substationID);
            partialResult = secretSharingRSAThreshold.partialEval(shares, fieldBase);
            partialObject.setPartialResult(partialResult);
            partialObject.setClientInfos(clientInfos);
        }

        partialObject.setFid(fid);
//      Send all shares
        URI uri = URI.create("http://localhost:3000/api/partials");
        try {
            httpAdapter.sendWithTimeout(uri, partialObject, 1000);
            log.info("Sent {} to {}", partialObject, uri);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }
}
