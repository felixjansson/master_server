package com.master_thesis.server.data;

import java.math.BigInteger;

public class HashIncomingData extends ComputationData {
    private BigInteger secretShare;

    public HashIncomingData() {
        super(Construction.HASH);
    }

    public BigInteger getSecretShare() {
        return secretShare;
    }

    public void setSecretShare(BigInteger secretShare) {
        this.secretShare = secretShare;
    }

}
