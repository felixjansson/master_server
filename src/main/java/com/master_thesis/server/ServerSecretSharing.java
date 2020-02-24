package com.master_thesis.server;

import java.util.List;

public interface ServerSecretSharing {

     int partialEval(List<Integer> shares);
     int partialProof(int proofComponent);
}
