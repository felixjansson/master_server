package com.master_thesis.server;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

@Component
public class HomomorphicHash {


    /**
     * This is the partial Eval function from the Homomorphic Hash based construction.
     * @param shares a list of all secret shares that is given to this server.
     * @return The sum of all shares (y_j)
     */
    public BigInteger partialEval(List<BigInteger> shares) {
        return shares.stream().reduce(BigInteger.ZERO, BigInteger::add);
    }

    /**
     * This is the partial proof function from the Homomorphic Hash based construction.
     * @param shares a list of all secret shares that is given to this server.
     * @return The partial proof, this is sigma_j in the paper.
     */
    public BigInteger partialProof(List<BigInteger> shares, BigInteger fieldBase, BigInteger generator) {
        BigInteger shareSum = shares.stream().reduce(BigInteger.ZERO, BigInteger::add);
        return hash(shareSum, fieldBase, generator);
    }


    public BigInteger hash(BigInteger input, BigInteger fieldBase, BigInteger generator) {
        return generator.modPow(input, fieldBase);
    }
}
