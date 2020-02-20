package com.master_thesis.server;

import java.util.List;

public interface ServerSecretSharing {

     int PartialEval(int serverID, List<Integer> secrets);
     int PartialProof(int secretKey, PublicParameters publicParameters, int proofComponent, int id);
}
