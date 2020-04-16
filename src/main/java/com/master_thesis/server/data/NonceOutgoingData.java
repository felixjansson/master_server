package com.master_thesis.server.data;

import java.math.BigInteger;

public class NonceOutgoingData extends ComputationData {

    private final BigInteger partialResult;
    private final BigInteger partialProof;
    private final BigInteger partialNonce;

    public NonceOutgoingData(int substationID, int fid, int id, BigInteger partialResult, BigInteger partialProof, BigInteger partialNonce) {
        super(substationID, fid, id, Construction.NONCE);
        this.partialResult = partialResult;
        this.partialProof = partialProof;
        this.partialNonce = partialNonce;
    }


    public BigInteger getPartialResult() {
        return partialResult;
    }

    public BigInteger getPartialProof() {
        return partialProof;
    }

    public BigInteger getPartialNonce() {
        return partialNonce;
    }
}
