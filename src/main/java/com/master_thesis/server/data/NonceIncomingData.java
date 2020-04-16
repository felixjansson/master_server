package com.master_thesis.server.data;

import java.math.BigInteger;

public class NonceIncomingData extends ComputationData {
    private BigInteger secretShare;
    private BigInteger nonceShare;

    public NonceIncomingData() {
        super(Construction.LINEAR);
    }

    public BigInteger getSecretShare() {
        return secretShare;
    }

    public void setSecretShare(BigInteger secretShare) {
        this.secretShare = secretShare;
    }

    public BigInteger getNonceShare() {
        return nonceShare;
    }

    public void setNonceShare(BigInteger nonceShare) {
        this.nonceShare = nonceShare;
    }
}
