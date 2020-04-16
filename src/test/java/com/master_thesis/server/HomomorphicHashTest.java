package com.master_thesis.server;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class HomomorphicHashTest {

    private HomomorphicHash homomorphicHash = new HomomorphicHash();
    BigInteger fieldBase = BigInteger.valueOf(7);
    BigInteger generator = BigInteger.valueOf(3);

    @Test
    void bigInt() {
        BigInteger ten = BigInteger.valueOf(10);
        BigInteger eleven = BigInteger.valueOf(11);

        assertEquals(ten.divide(eleven), BigInteger.ZERO);
        assertEquals(eleven.divide(ten), BigInteger.ONE);

    }

    @Test
    void partialEvalTest(){
        List<BigInteger> shares = Stream.of(1,4,5,2,1,34).map(BigInteger::valueOf).collect(Collectors.toList());
        BigInteger partialResult = homomorphicHash.partialEval(shares);
        assertEquals(BigInteger.valueOf(5), partialResult);
    }

}