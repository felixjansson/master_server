package com.master_thesis.server;

import ch.qos.logback.classic.Logger;
import org.ejml.simple.SimpleMatrix;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
public class RSAThreshold extends HomomorphicHash {
    private static final Logger log = (Logger) LoggerFactory.getLogger(HttpAdapter.class);


    public ClientInfo[] rsaPartialProof(List<RSAProofInfo> proofInformation, int transformatorID) {
        return proofInformation.stream()
                .map(client -> this.rsaPartialProofClient(client, transformatorID))
                .toArray(ClientInfo[]::new);
    }


    public SimpleMatrix getCofactorMatrix(SimpleMatrix a) {
        SimpleMatrix cofactor = a.copy();
        for (int i = 0; i < a.numRows(); i++) {
            for (int j = 0; j < a.numCols(); j++) {
                int sign = (i + j) % 2 == 0 ? 1 : -1;
                cofactor.set(i, j, createSubMatrix(a, i, j).determinant() * sign);
            }
        }
        return cofactor;
    }

    public static SimpleMatrix createSubMatrix(SimpleMatrix matrix, int excluding_row, int excluding_col) {
        SimpleMatrix mat = new SimpleMatrix(matrix.numRows() - 1, matrix.numCols() - 1);
        int r = -1;
        for (int i = 0; i < matrix.numRows(); i++) {
            if (i == excluding_row)
                continue;
            r++;
            int c = -1;
            for (int j = 0; j < matrix.numCols(); j++) {
                if (j == excluding_col)
                    continue;
                mat.set(r, ++c, matrix.get(i, j));
            }
        }
        return mat;
    }

    ClientInfo rsaPartialProofClient(RSAProofInfo rsaProofInfo, int transformatorID) {
        SimpleMatrix matrixOfClient = rsaProofInfo.getMatrixOfClient();

        // Create txt
        int t = matrixOfClient.numCols();
        SimpleMatrix squareMatrixOfClient = matrixOfClient.rows(0, t); // TODO: 2020-03-11 Handle when server < t

        SimpleMatrix adjugateMatrix = getCofactorMatrix(squareMatrixOfClient);

        // Find t rows from skShares
        SimpleMatrix skShares = rsaProofInfo.getSkShare().rows(0, t);

        BigInteger[] result = new BigInteger[t];
        BigInteger clientProof = rsaProofInfo.getClientProof();
        BigInteger rsaN = rsaProofInfo.getRsaN();

        for (int i = 0; i < result.length; i++) {
            long v = Math.round(2 * Math.round(adjugateMatrix.get(i, 0)) * skShares.get(i));
            BigInteger exponent = BigInteger.valueOf(v);
            try {
                result[i] = clientProof.modPow(exponent, rsaN);
            } catch (ArithmeticException e) {
                log.info("{}", rsaProofInfo);
                throw e;
            }
        }

        rsaProofInfo.setRsaDeterminant(squareMatrixOfClient.determinant());
        rsaProofInfo.setRsaProofComponent(result);
        return new ClientInfo(rsaProofInfo);
    }
}
