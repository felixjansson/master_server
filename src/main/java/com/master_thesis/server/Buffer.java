package com.master_thesis.server;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Buffer {

    private HashMap<Integer, Queue<Integer>> buffers;
    private PublicParameters publicParameters;
    private int transformatorID;

    public Buffer(PublicParameters publicParameters, int transformatorID) {
        this.publicParameters = publicParameters;
        this.transformatorID = transformatorID;
        buffers = new HashMap<>();
    }

    public void updateClients() {
        List<Integer> clientIDs = publicParameters.getClients(transformatorID);

        // Removes all client that are no longer connected to that transformator
        Set<Integer> keysToRemove = buffers.keySet();
        keysToRemove.removeIf(Predicate.not(clientIDs::contains));

        // add new clients
        clientIDs.forEach(id -> buffers.putIfAbsent(id, new LinkedList<>()));
    }

    public void putClientShare(ClientShare clientShare){
        updateClients();
        buffers.get(clientShare.getClientID()).add(clientShare.getShare());
    }

    public List<Integer> getShares(){
        return buffers.values().stream().map(Queue::poll).collect(Collectors.toList());
    }

    public boolean canCompute(){ // TODO: 2020-02-24 Check with PP how many clients
        updateClients();
        boolean noEmptyQueues = buffers.values().stream().noneMatch(Queue::isEmpty);
        return noEmptyQueues;
    }
}

