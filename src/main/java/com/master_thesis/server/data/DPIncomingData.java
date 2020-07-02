package com.master_thesis.server.data;

import java.math.BigInteger;

public class DPIncomingData extends ComputationData {
    private BigInteger secretShare;

    public DPIncomingData() {
        super(Construction.DP);
    }

    public BigInteger getSecretShare() {
        return secretShare;
    }

    public void setSecretShare(BigInteger secretShare) {
        this.secretShare = secretShare;
    }

}
