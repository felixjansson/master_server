package com.master_thesis.server;

import org.ejml.simple.SimpleMatrix;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigInteger;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.shadow.com.univocity.parsers.common.NormalizedString.toArray;

class RSAThresholdTest {

    @Test
    void getCofactor3x3() {
        double[][] data = new double[][]{{8, 2, 7}, {5, 4, 6}, {1, 3, 9}};
        SimpleMatrix a = new SimpleMatrix(data);
        double[][] correctData = new double[][]{{18, -39, 11}, {3, 65, -22}, {-16, -13, 22}};
        SimpleMatrix correct = new SimpleMatrix(correctData);
        RSAThreshold rsaThreshold = new RSAThreshold();
        SimpleMatrix cofactor = rsaThreshold.getCofactorMatrix(a);
        assertTrue(cofactor.isIdentical(correct, 0.00001), String.format("Correct:\n%s \nAnswer given:\n%s\n", correct, cofactor));
    }

    @Test
    void getCofactor2x2() {
        double[][] data = new double[][]{{8, 2}, {5, 4}};
        SimpleMatrix a = new SimpleMatrix(data);
        double[][] correctData = new double[][]{{4, -5}, {-2, 8}};
        SimpleMatrix correct = new SimpleMatrix(correctData);
        RSAThreshold rsaThreshold = new RSAThreshold();
        SimpleMatrix cofactor = rsaThreshold.getCofactorMatrix(a);
        assertTrue(cofactor.isIdentical(correct, 0.00001), String.format("Correct:\n%s \nAnswer given:\n%s\n", correct, cofactor));
    }

    @Test
    void getCofactor5x5() {
        double[][] data = new double[][]{{8, 2, 7, 13, 12}, {5, 4, 6, 11, 14}, {1, 3, 9, 10, 15}, {20, 21, 22, 23, 24}, {16, 17, 25, 18, 19}};
        SimpleMatrix a = new SimpleMatrix(data);
        double[][] correctData = new double[][]{{1800, 58, -171, -4680, 3091}, {-9310, 4451, 363, 13777, -9672}, {5937, -2555, -199, -7093, 4268}, {3766, -2961, 998, -5620, 3489}, {-3721, 2441, -1263, 5503, -3151}};
        SimpleMatrix correct = new SimpleMatrix(correctData);
        RSAThreshold rsaThreshold = new RSAThreshold();
        SimpleMatrix cofactor = rsaThreshold.getCofactorMatrix(a);
        assertTrue(cofactor.isIdentical(correct, 0.00001), String.format("Correct:\n%s \nAnswer given:\n%s\n", correct, cofactor));
    }
}