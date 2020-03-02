package com.master_thesis.server;

import java.math.BigInteger;
import java.util.List;

public interface ServerSecretSharing {

     BigInteger partialEval(List<BigInteger> shares, BigInteger fieldBase);
     BigInteger partialProof(List<BigInteger> shares, BigInteger fieldBase, BigInteger generator);
     BigInteger lastClientProof(List<BigInteger> nonces, BigInteger fieldBase, BigInteger generator);
}
