package com.master_thesis.server;

import java.math.BigInteger;
import java.util.Arrays;

public class ClientInfo {

    protected BigInteger rsaN;
    protected BigInteger clientProof;
    protected BigInteger[] rsaProofComponent;
    protected double rsaDeterminant;
    protected int publicKey;

    public ClientInfo(BigInteger clientProof) {
        this.clientProof = clientProof;
    }

    public ClientInfo(ClientInfo clientInfo) {
        this.rsaN = clientInfo.rsaN;
        this.clientProof = clientInfo.clientProof;
        this.rsaProofComponent = clientInfo.rsaProofComponent;
        this.rsaDeterminant = clientInfo.rsaDeterminant;
        this.publicKey = clientInfo.publicKey;
    }

    public ClientInfo(BigInteger rsaN, BigInteger clientProof, int publicKey) {
        this.rsaN = rsaN;
        this.clientProof = clientProof;
        this.publicKey = publicKey;
    }

    public BigInteger getRsaN() {
        return rsaN;
    }

    public void setRsaN(BigInteger rsaN) {
        this.rsaN = rsaN;
    }

    public BigInteger getClientProof() {
        return clientProof;
    }

    public void setClientProof(BigInteger clientProof) {
        this.clientProof = clientProof;
    }

    public BigInteger[] getRsaProofComponent() {
        return rsaProofComponent;
    }

    public void setRsaProofComponent(BigInteger[] rsaProofComponent) {
        this.rsaProofComponent = rsaProofComponent;
    }

    public double getRsaDeterminant() {
        return rsaDeterminant;
    }

    public void setRsaDeterminant(double rsaDeterminant) {
        this.rsaDeterminant = rsaDeterminant;
    }

    public int getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(int publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        return "ClientInfo{" +
                "rsaN=" + rsaN +
                ", clientProof=" + clientProof +
                ", rsaProofComponent=" + Arrays.toString(rsaProofComponent) +
                ", rsaDeterminant=" + rsaDeterminant +
                ", publicKey=" + publicKey +
                '}';
    }
}
