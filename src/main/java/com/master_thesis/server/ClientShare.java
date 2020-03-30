package com.master_thesis.server;


import org.ejml.simple.SimpleMatrix;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;

public class ClientShare {

    private int fid;
    private BigInteger share;
    private int clientID;
    private int substationID;
    private BigInteger proofComponent;
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

    public int getSubstationID() {
        return substationID;
    }

    public void setSubstationID(int substationID) {
        this.substationID = substationID;
    }

    public BigInteger getProofComponent() {
        return proofComponent;
    }

    public void setProofComponent(BigInteger proofComponent) {
        this.proofComponent = proofComponent;
    }

    public SimpleMatrix getMatrixOfClient() {
        return matrixOfClient;
    }

    public void setMatrixOfClient(byte[] matrixOfClient) {
        this.matrixOfClient = (SimpleMatrix) getObjectFromByteArray(matrixOfClient);
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
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
        if (rsaN != null) {
            this.rsaN = new BigInteger(rsaN);
        }
    }

    @Override
    public String toString() {
        return "ClientShare{" +
                "fid=" + fid +
                ", share=" + share +
                ", clientID=" + clientID +
                ", substationID=" + substationID +
                ", proofComponent=" + proofComponent +
                ", matrixOfClient=" + matrixOfClient +
                ", skShare=" + skShare +
                ", publicKey=" + publicKey +
                ", rsaN=" + rsaN +
                '}';
    }
}