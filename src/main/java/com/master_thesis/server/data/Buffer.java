package com.master_thesis.server.data;

import com.master_thesis.server.util.PublicParameters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void putClientShare(ComputationData clientShare) {
        substations.putIfAbsent(clientShare.getSubstationID(), new Substation());
        Substation substation = substations.get(clientShare.getSubstationID());
        substation.putIfAbsent(clientShare.getFid(), new Fid(clientShare.getConstruction()));
        Fid fid = substation.get(clientShare.getFid());

        fid.put(clientShare.getId(), clientShare);
    }

    public boolean canCompute(int substationID, int fid) { // TODO: 2020-02-24 Check with PP how many clients
        List<Integer> clientIDs = publicParameters.getClients(substationID, fid);
        return substations.get(substationID).get(fid).keySet().containsAll(clientIDs);
    }

    private static class Substation extends HashMap<Integer, Fid> {
    }

    public static class Fid extends HashMap<Integer, ComputationData> {
        private Construction construction;

        public Fid(Construction construction) {
            this.construction = construction;
        }

        public Construction getConstruction() {
            return construction;
        }

    }

}

