package com.master_thesis.server;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.master_thesis.server.data.*;
import com.master_thesis.server.util.HttpAdapter;
import com.master_thesis.server.util.PublicParameters;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@SpringBootApplication
@RestController
@RequestMapping(value = "/api")
public class ServerApplication {
    private static final Logger log = (Logger) LoggerFactory.getLogger(ServerApplication.class);
    private final URI verifier = URI.create("http://localhost:3000/api/server/");
    private PublicParameters publicParameters;
    private HomomorphicHash secretSharingHomomorphicHash;
    private RSAThreshold secretSharingRSAThreshold;
    private HttpAdapter httpAdapter;
    private Buffer buffer;
    private int serverID;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public ServerApplication(PublicParameters publicParameters, HomomorphicHash secretSharingHomomorphicHash, RSAThreshold secretSharingRSAThreshold, HttpAdapter httpAdapter) {
        this.publicParameters = publicParameters;
        this.secretSharingHomomorphicHash = secretSharingHomomorphicHash;
        this.secretSharingRSAThreshold = secretSharingRSAThreshold;
        this.httpAdapter = httpAdapter;
        this.buffer = new Buffer(publicParameters);
        this.serverID = publicParameters.getServerID();
        new Thread(() -> {
            boolean running = true;
            while (running) {
                System.out.println("Enter r to re-register server");
                Scanner input = new Scanner(System.in);
                switch (input.next()) {
                    case "r":
                        publicParameters.reRegisterServer();
                        serverID = publicParameters.getServerID();
                        break;
                    case "q":
                        running = false;
                        break;
                }
            }

        }).start();
    }

    @SneakyThrows
    @PostMapping(value = "/hash-data")
    void receiveHashShare(@RequestBody HashIncomingData dataFromClient) {
        log.debug("Received share: {} ", objectMapper.writeValueAsString(dataFromClient));
        buffer.putClientShare(dataFromClient);
        if (buffer.canCompute(dataFromClient.getSubstationID(), dataFromClient.getFid())) {
            new Thread(() -> performComputations(dataFromClient.getSubstationID(), dataFromClient.getFid())).start();
        }
    }

    @PostMapping(value = "/rsa-data")
    void receiveRSAShare(@RequestBody RSAIncomingData dataFromClient) {
        log.debug("Received share: {} ", dataFromClient);
        buffer.putClientShare(dataFromClient);
        if (buffer.canCompute(dataFromClient.getSubstationID(), dataFromClient.getFid())) {
            new Thread(() -> performComputations(dataFromClient.getSubstationID(), dataFromClient.getFid())).start();
        }
    }

    private void performComputations(int substationID, int fid) {
        log.info("=== Computing partial information for Substation {} fid {} === ", substationID, fid);
        Buffer.Fid fidData = buffer.getFid(substationID, fid);

//      Compute all common operations
        BigInteger fieldBase = publicParameters.getFieldBase(substationID);
        BigInteger generator = publicParameters.getGenerator(substationID);

        Construction construction = fidData.getConstruction();

//      Compute proofs for HomomorphicHash construction
        if (construction.equals(Construction.HASH)) {
            List<HashIncomingData> computationData = fidData.values().stream().map(v -> (HashIncomingData) v).collect(Collectors.toList());
            List<BigInteger> shares = computationData.stream().map(HashIncomingData::getSecretShare).collect(Collectors.toList());
            BigInteger partialProof = secretSharingHomomorphicHash.homomorphicPartialProof(shares, fieldBase, generator);
            BigInteger partialResult = secretSharingHomomorphicHash.partialEval(shares, fieldBase);
            httpAdapter.sendWithTimeout(verifier.resolve(Construction.HASH.getEndpoint()), new HashOutgoingData(substationID, fid, serverID, partialResult, partialProof), 3000);
        }

//      Compute proofs for RSAThreshold construction
        if (construction.equals(Construction.RSA)) {
            List<RSAIncomingData> computationData = fidData.values().stream().map(v -> (RSAIncomingData) v).collect(Collectors.toList());
            List<BigInteger> shares = computationData.stream().map(RSAIncomingData::getShare).collect(Collectors.toList());
            BigInteger partialResult = secretSharingRSAThreshold.partialEval(shares, fieldBase);
            Map<Integer, RSAOutgoingData.ProofData> partialProofs = secretSharingRSAThreshold.rsaPartialProof(computationData);
            httpAdapter.sendWithTimeout(verifier.resolve(Construction.RSA.getEndpoint()), new RSAOutgoingData(substationID, fid, serverID, partialResult, partialProofs), 3000);
        }
    }


    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
