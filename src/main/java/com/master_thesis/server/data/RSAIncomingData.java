package com.master_thesis.server.data;

import ch.qos.logback.classic.Logger;
import org.ejml.simple.SimpleMatrix;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;

public class RSAIncomingData extends ComputationData {


    private static final Logger log = (Logger) LoggerFactory.getLogger(RSAIncomingData.class);
    private BigInteger share, proofComponent, rsaN;
    private SimpleMatrix matrixOfClient, skShare;

    protected RSAIncomingData() {
        super(Construction.RSA);
    }

    public BigInteger getShare() {
        return share;
    }

    public void setShare(BigInteger share) {
        this.share = share;
    }

    public BigInteger getProofComponent() {
        return proofComponent;
    }

    public void setProofComponent(BigInteger proofComponent) {
        this.proofComponent = proofComponent;
    }

    public BigInteger getRsaN() {
        return rsaN;
    }

    public void setRsaN(BigInteger rsaN) {
        this.rsaN = rsaN;
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

}
