package com.master_thesis.server;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

@Component
public class HomomorphicHash {


    public BigInteger partialEval(List<BigInteger> shares, BigInteger fieldBase) {
        return shares.stream().reduce(BigInteger.ZERO, BigInteger::add).mod(fieldBase);
    }

    public BigInteger homomorphicPartialProof(List<BigInteger> shares, BigInteger fieldBase, BigInteger generator) {
        BigInteger shareSum = shares.stream().reduce(BigInteger.ZERO, BigInteger::add);
        return hash(shareSum, fieldBase, generator);
    }

    public BigInteger lastClientProof(List<BigInteger> nonces, BigInteger fieldBase, BigInteger generator) {
        BigInteger totient = eulerTotient(fieldBase);
        BigInteger nonceSum = nonces.stream().reduce(BigInteger.ZERO, BigInteger::add);
        BigDecimal sum = new BigDecimal(nonceSum);
        BigDecimal tot = new BigDecimal(totient);
        BigInteger ceil = sum.divide(tot, RoundingMode.CEILING).toBigInteger();
        BigInteger result = totient.multiply(ceil).subtract(nonceSum).mod(fieldBase);
        assert result.divide(fieldBase).equals(BigInteger.ZERO); // TODO: 2020-03-04 Remove this check. (Check if res in field)
        return hash(result, fieldBase, generator);
    }

    public BigInteger lastClientProofInverse(List<BigInteger> nonces, BigInteger fieldBase, BigInteger generator) {
        BigInteger nonceSum = nonces.stream().reduce(BigInteger.ZERO, BigInteger::add);
        BigInteger inverse = generator.modInverse(fieldBase);
        return inverse.modPow(nonceSum, fieldBase);
    }


    public BigInteger hash(BigInteger input, BigInteger fieldBase, BigInteger generator) {
        return generator.modPow(input, fieldBase);
    }

    private BigInteger eulerTotient(BigInteger prime) {
        if (!prime.isProbablePrime(16)) {
            throw new RuntimeException("No prime, no totient");
        }
        return prime.subtract(BigInteger.ONE);
    }

}
