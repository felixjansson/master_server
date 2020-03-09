package com.master_thesis.server;

import org.ejml.simple.SimpleMatrix;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
public class RSAThreshold extends HomomorphicHash {

    public ClientInfo[] rsaPartialProof(List<RSAProofInfo> proofInformation) {
        return proofInformation.stream().map(this::rsaPartialProofClient).toArray(ClientInfo[]::new);
    }

    private ClientInfo rsaPartialProofClient(RSAProofInfo rsaProofInfo) {
        SimpleMatrix matrixOfClient = rsaProofInfo.getMatrixOfClient();

        int t = matrixOfClient.numCols();
        SimpleMatrix squareMatrixOfClient = matrixOfClient.rows(0, t);

        double det = squareMatrixOfClient.determinant();
        SimpleMatrix squareInverse = squareMatrixOfClient.invert();
        SimpleMatrix adjugateMatrix = squareInverse.scale(det);
        SimpleMatrix skShares = rsaProofInfo.getSkShare().rows(0, t);
        BigInteger[] result = new BigInteger[t];
        BigInteger clientProof = rsaProofInfo.getClientProof();
        BigInteger rsaN = rsaProofInfo.getRsaN();

        for (int i = 0; i < result.length; i++) {
            BigInteger exponent = BigInteger.valueOf((long) (2 * adjugateMatrix.get(i, 0) * skShares.get(i)));
            result[i] = clientProof.modPow(exponent, rsaN);
        }

        rsaProofInfo.setRsaDeterminant(det);
        rsaProofInfo.setRsaProofComponent(result);
        return new ClientInfo(rsaProofInfo);
    }
}
