package com.master_thesis.server;


import ch.qos.logback.classic.Logger;
import org.ejml.simple.SimpleMatrix;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;

public class ClientShare {

    private static final Logger log = (Logger) LoggerFactory.getLogger(ClientShareController.class);
    private BigInteger share;
    private int clientID;
    private int transformatorID;
    private BigInteger proofComponent;
    private BigInteger nonce;
    private SimpleMatrix matrixOfClient;
    private SimpleMatrix skShare;
    private int publicKey;
    private BigInteger rsaN;

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

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }

    public SimpleMatrix getMatrixOfClient() {
        return matrixOfClient;
    }

    public void setMatrixOfClient(byte[] matrixOfClient) {
        this.matrixOfClient = (SimpleMatrix) getObjectFromByteArray(matrixOfClient);
    }

    private Object getObjectFromByteArray(byte[] arr) {
        ByteArrayInputStream bios = new ByteArrayInputStream(arr);
        try {
            ObjectInputStream ios = new ObjectInputStream(bios);
            return ios.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SimpleMatrix getSkShare() {
        return skShare;
    }

    public void setSkShare(byte[] skShare) {
        this.skShare = (SimpleMatrix) getObjectFromByteArray(skShare);
    }

    @Override
    public String toString() {
        return "ClientShare{" +
                "share=" + share +
                ", clientID=" + clientID +
                ", transformatorID=" + transformatorID +
                ", proofComponent=" + proofComponent +
                ", nonce=" + nonce +
                ", matrixOfClient=" + matrixOfClient +
                ", skShare=" + skShare +
                ", publicKey=" + publicKey +
                ", rsaN=" + rsaN +
                '}';
    }

    public int getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(int publicKey) {
        this.publicKey = publicKey;
    }

    public BigInteger getRsaN() {
        return rsaN;
    }

    public void setRsaN(String rsaN) {
        this.rsaN = new BigInteger(rsaN);
    }

}