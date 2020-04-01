package com.master_thesis.server.data;

import java.math.BigInteger;

public class HashOutgoingData extends ComputationData {

    private BigInteger partialResult, partialProof;

    public HashOutgoingData(int substationID, int fid, int id, BigInteger partialResult, BigInteger partialProof) {
        super(substationID, fid, id, Construction.HASH);
        this.partialProof = partialProof;
        this.partialResult = partialResult;
    }

    public BigInteger getPartialResult() {
        return partialResult;
    }

    public BigInteger getPartialProof() {
        return partialProof;
    }

    @Override
    public String toString() {
        return "HashOutgoingData{" +
                "partialResult=" + partialResult +
                ", partialProof=" + partialProof +
                "} " + super.toString();
    }

}
