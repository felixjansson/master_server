package com.master_thesis.server;

import java.math.BigInteger;
import java.util.Arrays;

public class PartialObject {

    private BigInteger partialResult;
    private int substationID;
    private int serverID;
    private BigInteger homomorphicPartialProof;
    private ClientInfo[] clientInfos;
    private int fid;

    public PartialObject(BigInteger partialResult, int substationID, int serverID) {
        this.partialResult = partialResult;
        this.substationID = substationID;
        this.serverID = serverID;
    }

    public BigInteger getPartialResult() {
        return partialResult;
    }

    public void setPartialResult(BigInteger partialResult) {
        this.partialResult = partialResult;
    }

    public int getSubstationID() {
        return substationID;
    }

    public void setSubstationID(int substationID) {
        this.substationID = substationID;
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

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    @Override
    public String toString() {
        return "PartialObject{" +
                "partialResult=" + partialResult +
                ", substationID=" + substationID +
                ", serverID=" + serverID +
                ", homomorphicPartialProof=" + homomorphicPartialProof +
                ", clientInfos=" + Arrays.toString(clientInfos) +
                '}';
    }
}
