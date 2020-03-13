package com.master_thesis.server;

import org.ejml.simple.SimpleMatrix;

import java.math.BigInteger;
import java.util.Arrays;

public class RSAProofInfo extends ClientInfo {

    private SimpleMatrix matrixOfClient;
    private SimpleMatrix skShare;


    public RSAProofInfo(BigInteger rsaN, BigInteger clientProof, int publicKey, SimpleMatrix matrixOfClient, SimpleMatrix skShare) {
        super(rsaN, clientProof, publicKey);
        this.matrixOfClient = matrixOfClient;
        this.skShare = skShare;
    }

    public RSAProofInfo(RSAProofInfo defaultValues, BigInteger lastClientProof) {
        this(
                defaultValues.rsaN,
                lastClientProof,
                defaultValues.publicKey,
                defaultValues.matrixOfClient,
                defaultValues.skShare
        );
    }

    public SimpleMatrix getMatrixOfClient() {
        return matrixOfClient;
    }

    public void setMatrixOfClient(SimpleMatrix matrixOfClient) {
        this.matrixOfClient = matrixOfClient;
    }

    public SimpleMatrix getSkShare() {
        return skShare;
    }

    public void setSkShare(SimpleMatrix skShare) {
        this.skShare = skShare;
    }

    @Override
    public String toString() {
        return "RSAProofInfo{" +
                "matrixOfClient=" + matrixOfClient +
                ", skShare=" + skShare +
                ", rsaN=" + rsaN +
                ", clientProof=" + clientProof +
                ", rsaProofComponent=" + Arrays.toString(rsaProofComponent) +
                ", rsaDeterminant=" + rsaDeterminant +
                ", publicKey=" + publicKey +
                '}';
    }
}
