package com.master_thesis.server;

import java.math.BigInteger;
import java.util.Arrays;

public class PartialObject {

    private BigInteger partialResult;
    private int transformatorID;
    private int serverID;
    private BigInteger homomorphicPartialProof;
    private ClientInfo[] clientInfos;

    public PartialObject(BigInteger partialResult, int transformatorID, int serverID) {
        this.partialResult = partialResult;
        this.transformatorID = transformatorID;
        this.serverID = serverID;
    }

    public BigInteger getPartialResult() {
        return partialResult;
    }

    public void setPartialResult(BigInteger partialResult) {
        this.partialResult = partialResult;
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

    public BigInteger getHomomorphicPartialProof() {
        return homomorphicPartialProof;
    }

    public void setHomomorphicPartialProof(BigInteger homomorphicPartialProof) {
        this.homomorphicPartialProof = homomorphicPartialProof;
    }

    public ClientInfo[] getClientInfos() {
        return clientInfos;
    }

    public void setClientInfos(ClientInfo[] clientInfos) {
        this.clientInfos = clientInfos;
    }

    @Override
    public String toString() {
        return "PartialObject{" +
                "partialResult=" + partialResult +
                ", transformatorID=" + transformatorID +
                ", serverID=" + serverID +
                ", homomorphicPartialProof=" + homomorphicPartialProof +
                ", clientInfos=" + Arrays.toString(clientInfos) +
                '}';
    }
}
