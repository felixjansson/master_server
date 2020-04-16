package com.master_thesis.server;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

@Component
public class HomomorphicHash {


    public BigInteger partialEval(List<BigInteger> shares) {
        return shares.stream().reduce(BigInteger.ZERO, BigInteger::add);
    }

    public BigInteger homomorphicPartialProof(List<BigInteger> shares, BigInteger fieldBase, BigInteger generator) {
        BigInteger shareSum = shares.stream().reduce(BigInteger.ZERO, BigInteger::add);
        return hash(shareSum, fieldBase, generator);
    }


    public BigInteger hash(BigInteger input, BigInteger fieldBase, BigInteger generator) {
        return generator.modPow(input, fieldBase);
    }
}
