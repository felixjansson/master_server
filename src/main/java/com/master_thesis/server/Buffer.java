package com.master_thesis.server;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Buffer {

    private Map<Integer, Substation> substations;
    private PublicParameters publicParameters;

    public Buffer(PublicParameters publicParameters) {
        this.publicParameters = publicParameters;
        substations = new HashMap<>();
    }

    public Fid getFid(int substationID, int fid) {
        return substations.get(substationID).get(fid);
    }

    public void putClientShare(ClientShare clientShare) {

        substations.putIfAbsent(clientShare.getSubstationID(), new Substation());
        Substation substation = substations.get(clientShare.getSubstationID());
        substation.putIfAbsent(clientShare.getFid(), new Fid());
        Fid fid = substation.get(clientShare.getFid());

        fid.put(clientShare.getClientID(), clientShare);
    }

    public boolean canCompute(int substationID, int fid) { // TODO: 2020-02-24 Check with PP how many clients
        List<Integer> clientIDs = publicParameters.getClients(substationID, fid);
        return substations.get(substationID).get(fid).keySet().containsAll(clientIDs);
    }

    private class Substation extends HashMap<Integer, Fid> {
    }

    public class Fid extends HashMap<Integer, ClientShare> {

        public List<BigInteger> getShares() {
            return values().stream().map(ClientShare::getShare).collect(Collectors.toList());
        }

        public List<RSAProofInfo> getRSAProofInformation() {
            return values().stream()
                    .filter(Objects::nonNull)
                    .map(share -> new RSAProofInfo(
                            share.getRsaN(),
                            share.getProofComponent(),
                            share.getPublicKey(),
                            share.getMatrixOfClient(),
                            share.getSkShare()
                    ))
                    .collect(Collectors.toList());
        }
    }

}

