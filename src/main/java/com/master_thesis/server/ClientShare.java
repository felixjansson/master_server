package com.master_thesis.server;


import java.math.BigInteger;

public class ClientShare {

    private BigInteger share;
    private int clientID;



    private int transformatorID;
    private BigInteger proofComponent;

    public BigInteger getShare() {
        return share;
    }

    public void setShare(BigInteger share) {
        this.share = share;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public int getTransformatorID() {
        return transformatorID;
    }

    public void setTransformatorID(int transformatorID) {
        this.transformatorID = transformatorID;
    }

    public BigInteger getProofComponent() {
        return proofComponent;
    }

    public void setProofComponent(BigInteger proofComponent) {
        this.proofComponent = proofComponent;
    }

    public BigInteger getNonce() {
        return nonce;
    }

    @Override
    public String toString() {
        return "ClientShare{" +
                "share=" + share +
                ", clientID=" + clientID +
                ", transformatorID=" + transformatorID +
                ", proofComponent=" + proofComponent +
                ", nonce=" + nonce +
                '}';
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }

    private BigInteger nonce;

}