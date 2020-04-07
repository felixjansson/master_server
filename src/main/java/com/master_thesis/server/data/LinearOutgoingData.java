package com.master_thesis.server.data;

import java.math.BigInteger;

public class LinearOutgoingData extends ComputationData {

    private BigInteger partialResult;

    public LinearOutgoingData(int substationID, int fid, int serverID, BigInteger partialResult) {
        super(substationID,fid, serverID, Construction.LINEAR);

        this.partialResult = partialResult;
    }

    public BigInteger getPartialResult() {
        return partialResult;
    }
}
