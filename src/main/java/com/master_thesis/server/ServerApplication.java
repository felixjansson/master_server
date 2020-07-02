package com.master_thesis.server;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.master_thesis.server.data.*;
import com.master_thesis.server.util.HttpAdapter;
import com.master_thesis.server.util.PublicParameters;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    // ******************************************
    // Here we define variables that will be used in the different constructions and for communication.

    private static final Logger log = (Logger) LoggerFactory.getLogger(ServerApplication.class);
    private final URI verifier = URI.create("http://localhost:3000/api/server/");
    private PublicParameters publicParameters;
    private HomomorphicHash homomorphicHash;
    private RSAThreshold rsaThreshold;
    private LinearSignature linearSignature;
    private DifferentialPrivacy differentialPrivacy;
    private HttpAdapter httpAdapter;
    private Buffer buffer;
    private int serverID;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public ServerApplication(PublicParameters publicParameters, @Qualifier("homomorphicHash") HomomorphicHash homomorphicHash, RSAThreshold rsaThreshold, LinearSignature linearSignature, DifferentialPrivacy differentialPrivacy, HttpAdapter httpAdapter) {
        this.publicParameters = publicParameters;
        this.homomorphicHash = homomorphicHash;
        this.rsaThreshold = rsaThreshold;
        this.linearSignature = linearSignature;
        this.differentialPrivacy = differentialPrivacy;
        this.httpAdapter = httpAdapter;
        this.buffer = new Buffer(publicParameters);
        this.serverID = publicParameters.getServerID();
        new Thread(() -> {
            boolean running = true;
            while (running) {
                System.out.println("Enter r to re-register server, [l]ist servers");
                Scanner input = new Scanner(System.in);
                switch (input.next()) {
                    case "r":
                        publicParameters.reRegisterServer();
                        serverID = publicParameters.getServerID();
                        break;
                    case "l":
                        System.out.println(publicParameters.getServerList());

                }
            }

        }).start();
    }

    // ******************************************

    /**
     * This is the function that will call all computations for the server from the paper.
     * Note that not all constructions are called every time.
     * @param substationID the substation the computation is performed for.
     * @param fid id for the computation.
     */
    private void performComputations(int substationID, int fid) {
        log.info("=== Computing partial information for Substation {} fid {} === ", substationID, fid);
        Buffer.Fid fidData = buffer.getFid(substationID, fid);

//      Compute all common operations
        BigInteger fieldBase = publicParameters.getFieldBase(substationID);
        BigInteger generator = publicParameters.getGenerator(substationID);

        Construction construction = fidData.getConstruction();

//      Compute partial result and proof for Homomorphic Hash construction
        if (construction.equals(Construction.HASH)) {
            // We retrieve all data related to this computation of the homomorphic hash construction.
            List<HashIncomingData> computationData = fidData.values().stream().map(v -> (HashIncomingData) v).collect(Collectors.toList());
            List<BigInteger> shares = computationData.stream().map(HashIncomingData::getSecretShare).collect(Collectors.toList());

            // Compute the partial proof function from the shares.
            BigInteger partialProof = homomorphicHash.partialProof(shares, fieldBase, generator);

            // Compute the final eval function from the shares.
            BigInteger partialResult = homomorphicHash.partialEval(shares);

            // Make the result of both computations available to others by sending it to the verifier.
            httpAdapter.sendWithTimeout(verifier.resolve(Construction.HASH.getEndpoint()), new HashOutgoingData(substationID, fid, serverID, partialResult, partialProof), 3000);
        }

//      Compute partial result and proof for Threshold Signature construction
        if (construction.equals(Construction.RSA)) {
            // We retrieve all data related to this computation of the Threshold Signature construction.
            List<RSAIncomingData> computationData = fidData.values().stream().map(v -> (RSAIncomingData) v).collect(Collectors.toList());
            List<BigInteger> shares = computationData.stream().map(RSAIncomingData::getShare).collect(Collectors.toList());

            // Compute the partial proof function for Threshold Signature construction from the paper.
            Map<Integer, RSAOutgoingData.ProofData> partialProofs = rsaThreshold.partialProof(computationData);

            // Compute the final eval function from the shares.
            BigInteger partialResult = rsaThreshold.partialEval(shares);

            // Make the result of both computations available to others by sending it to the verifier.
            httpAdapter.sendWithTimeout(verifier.resolve(Construction.RSA.getEndpoint()), new RSAOutgoingData(substationID, fid, serverID, partialResult, partialProofs), 3000);
        }

//      Compute partial result for Linear Signature construction
        if (construction.equals(Construction.LINEAR)) {
            // We retrieve all data related to this computation of the Linear Signature construction.
            List<LinearIncomingData> computationData = fidData.values().stream().map(v -> (LinearIncomingData) v).collect(Collectors.toList());
            List<BigInteger> shares = computationData.stream().map(LinearIncomingData::getSecretShare).collect(Collectors.toList());

            // Compute the partial proof function from the shares using the Linear Signature construction.
            BigInteger partialResult = linearSignature.partialEval(shares);

            // Make the result of both computations available to others by sending it to the verifier.
            httpAdapter.sendWithTimeout(verifier.resolve(Construction.LINEAR.getEndpoint()), new LinearOutgoingData(substationID, fid, serverID, partialResult), 3000);
        }

//      Compute partial result and proof for Differential Privacy construction
        if (construction.equals(Construction.DP)) {
            // We retrieve all data related to this computation of the homomorphic hash construction.
            List<DPIncomingData> computationData = fidData.values().stream().map(v -> (DPIncomingData) v).collect(Collectors.toList());
            List<BigInteger> shares = computationData.stream().map(DPIncomingData::getSecretShare).collect(Collectors.toList());

            // Compute the partial proof function from the shares.
            BigInteger partialProof = differentialPrivacy.partialProof(shares, fieldBase, generator);

            // Compute the final eval function from the shares.
            BigInteger partialResult = differentialPrivacy.partialEval(shares);

            // Make the result of both computations available to others by sending it to the verifier.
            httpAdapter.sendWithTimeout(verifier.resolve(Construction.DP.getEndpoint()), new DPOutgoingData(substationID, fid, serverID, partialResult, partialProof), 3000);
        }

    }


    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }


    // ******************************************
    // The following functions are there to receive data and when every client has sent their data a computation is started.

    @SneakyThrows
    @PostMapping(value = "/hash-data")
    void receiveHashShare(@RequestBody HashIncomingData dataFromClient) { log.debug("Received share: {} ", objectMapper.writeValueAsString(dataFromClient));
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

    @PostMapping(value = "/linear-data")
    void receiveLinearShare(@RequestBody LinearIncomingData dataFromClient) {
        log.debug("Received share: {} ", dataFromClient);
        buffer.putClientShare(dataFromClient);
        if (buffer.canCompute(dataFromClient.getSubstationID(), dataFromClient.getFid())) {
            new Thread(() -> performComputations(dataFromClient.getSubstationID(), dataFromClient.getFid())).start();
        }
    }

    @SneakyThrows
    @PostMapping(value = "/dp-data")
    void receiveDPShare(@RequestBody DPIncomingData dataFromClient) {
        log.debug("Received share: {} ", objectMapper.writeValueAsString(dataFromClient));
        buffer.putClientShare(dataFromClient);
        if (buffer.canCompute(dataFromClient.getSubstationID(), dataFromClient.getFid())) {
            new Thread(() -> performComputations(dataFromClient.getSubstationID(), dataFromClient.getFid())).start();
        }
    }


}
