package com.master_thesis.server.data;

import java.math.BigInteger;

public class LinearIncomingData extends ComputationData {

    private BigInteger secretShare;

    public LinearIncomingData() {
        super(Construction.LINEAR);
    }

    public BigInteger getSecretShare() {
        return secretShare;
    }

    public void setSecretShare(BigInteger secretShare) {
        this.secretShare = secretShare;
    }
}
