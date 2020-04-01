package com.master_thesis.server.data;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map;

public class RSAOutgoingData extends ComputationData {

    private BigInteger partialResult;
    private Map<Integer, ProofData> partialProofs;

    public RSAOutgoingData(int fid, int substationID, int id, BigInteger partialResult, Map<Integer, ProofData> partialProofs) {
        super(fid, substationID, id, Construction.RSA);
        this.partialResult = partialResult;
        this.partialProofs = partialProofs;
    }

    public BigInteger getPartialResult() {
        return partialResult;
    }

    public Map<Integer, ProofData> getPartialProofs() {
        return partialProofs;
    }

    @Override
    public String toString() {
        return "RSAOutgoingData{" +
                "partialResult=" + partialResult +
                ", partialProofs=" + partialProofs +
                "} " + super.toString();
    }

    public static class ProofData {

        private BigInteger rsaN;
        private BigInteger[] rsaProofComponent;
        private double rsaDeterminant;
        private BigInteger clientProof;

        public ProofData(BigInteger rsaN, BigInteger[] rsaProofComponent, double rsaDeterminant, BigInteger clientProof) {
            this.rsaN = rsaN;
            this.rsaProofComponent = rsaProofComponent;
            this.rsaDeterminant = rsaDeterminant;
            this.clientProof = clientProof;
        }

        public BigInteger getRsaN() {
            return rsaN;
        }

        public BigInteger[] getRsaProofComponent() {
            return rsaProofComponent;
        }

        public double getRsaDeterminant() {
            return rsaDeterminant;
        }

        public BigInteger getClientProof() {
            return clientProof;
        }

        @Override
        public String toString() {
            return "ProofData{" +
                    "rsaN=" + rsaN +
                    ", rsaProofComponent=" + Arrays.toString(rsaProofComponent) +
                    ", rsaDeterminant=" + rsaDeterminant +
                    ", clientProof=" + clientProof +
                    '}';
        }
    }
}
