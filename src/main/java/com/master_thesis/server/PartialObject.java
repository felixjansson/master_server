package com.master_thesis.server;

import java.math.BigInteger;
import java.util.List;

public class PartialObject {

    private BigInteger partialResult;
    private BigInteger serverPartialProof;
    private List<BigInteger> clientPartialProof;
    private int transformatorID;
    private int serverID;

    public PartialObject(BigInteger partialResult, BigInteger serverPartialProof, List<BigInteger> clientPartialProof, int transformatorID, int serverID) {
        this.partialResult = partialResult;
        this.serverPartialProof = serverPartialProof;
        this.clientPartialProof = clientPartialProof;
        this.transformatorID = transformatorID;
        this.serverID = serverID;
    }

    public BigInteger getPartialResult() {
        return partialResult;
    }

    public void setPartialResult(BigInteger partialResult) {
        this.partialResult = partialResult;
    }

    public BigInteger getServerPartialProof() {
        return serverPartialProof;
    }

    public void setServerPartialProof(BigInteger serverPartialProof) {
        this.serverPartialProof = serverPartialProof;
    }

    public List<BigInteger> getClientPartialProof() {
        return clientPartialProof;
    }

    public void setClientPartialProof(List<BigInteger> clientPartialProof) {
        this.clientPartialProof = clientPartialProof;
    }

    public int getTransformatorID() {
        return transformatorID;
    }

    public void setTransformatorID(int transformatorID) {
        this.transformatorID = transformatorID;
    }

    public int getServerID() {
        return serverID;
    }

    public void setServerID(int serverID) {
        this.serverID = serverID;
    }

    @Override
    public String toString() {
        return "PartialObject{" +
                "partialResult=" + partialResult +
                ", serverPartialProof=" + serverPartialProof +
                ", clientPartialProof=" + clientPartialProof +
                ", transformatorID=" + transformatorID +
                ", serverID=" + serverID +
                '}';
    }
}
