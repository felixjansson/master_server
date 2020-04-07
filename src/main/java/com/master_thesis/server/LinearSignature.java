package com.master_thesis.server;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
public class LinearSignature {

    public BigInteger partialEval(List<BigInteger> shares, BigInteger fieldBase) {
        return shares.stream().reduce(BigInteger.ZERO, BigInteger::add).mod(fieldBase);
    }
}
