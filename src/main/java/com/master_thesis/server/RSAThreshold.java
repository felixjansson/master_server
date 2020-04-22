package com.master_thesis.server;

import ch.qos.logback.classic.Logger;
import com.master_thesis.server.data.RSAIncomingData;
import com.master_thesis.server.data.RSAOutgoingData;
import com.master_thesis.server.util.HttpAdapter;
import org.ejml.simple.SimpleMatrix;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RSAThreshold {
    private static final Logger log = (Logger) LoggerFactory.getLogger(HttpAdapter.class);

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

    public BigInteger partialEval(List<BigInteger> shares) {
        return shares.stream().reduce(BigInteger.ZERO, BigInteger::add);
    }

    public Map<Integer, RSAOutgoingData.ProofData> rsaPartialProof(List<RSAIncomingData> proofInformation) {
        return proofInformation.stream()
                .collect(Collectors.toMap(RSAIncomingData::getId, this::rsaPartialProofClient));
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

    RSAOutgoingData.ProofData rsaPartialProofClient(RSAIncomingData rsaProofInfo) {
        SimpleMatrix matrixOfClient = rsaProofInfo.getMatrixOfClient();

        // Create txt
        int t = matrixOfClient.numCols();
        SimpleMatrix squareMatrixOfClient = matrixOfClient.rows(0, t); // TODO: 2020-03-11 Handle when server < t

        SimpleMatrix adjugateMatrix = getCofactorMatrix(squareMatrixOfClient).transpose();

        // Find t rows from skShares
        SimpleMatrix skShares = rsaProofInfo.getSkShare().rows(0, t);

        BigInteger[] result = new BigInteger[t];
        BigInteger clientProof = rsaProofInfo.getProofComponent();
        BigInteger rsaN = rsaProofInfo.getRsaN();

        for (int i = 0; i < result.length; i++) {
            long v = Math.round(2 * Math.round(adjugateMatrix.get(0, i)) * skShares.get(i));
            BigInteger exponent = BigInteger.valueOf(v);
            try {
                result[i] = clientProof.modPow(exponent, rsaN);
            } catch (ArithmeticException e) {
                log.info("{}", rsaProofInfo);
                throw e;
            }
        }
        return new RSAOutgoingData.ProofData(rsaN, result, squareMatrixOfClient.determinant(), clientProof);
    }
}
