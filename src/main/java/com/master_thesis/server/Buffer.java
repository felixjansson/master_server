package com.master_thesis.server;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Buffer {

    private HashMap<Integer, Queue<ClientShare>> buffers;
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

    public void putClientShare(ClientShare clientShare) {
        updateClients();
        buffers.get(clientShare.getClientID()).add(clientShare);
    }

    public List<BigInteger> getNonces() {
        return buffers.values().stream()
                .map(Queue::peek)
                .filter(Objects::nonNull)
                .map(ClientShare::getNonce)
                .collect(Collectors.toList());
    }

    public List<BigInteger> getProofComponents() {
        return buffers.values().stream()
                .map(Queue::peek)
                .filter(Objects::nonNull)
                .map(ClientShare::getProofComponent)
                .collect(Collectors.toList());
    }

    public List<BigInteger> getShares() {
        return buffers.values().stream()
                .map(Queue::peek)
                .filter(Objects::nonNull)
                .map(ClientShare::getShare)
                .collect(Collectors.toList());
    }

    public void remove() {
        buffers.values().forEach(Queue::remove);
    }

    public boolean canCompute() { // TODO: 2020-02-24 Check with PP how many clients
        updateClients();
        boolean noEmptyQueues = buffers.values().stream().noneMatch(Queue::isEmpty);
        return noEmptyQueues;
    }

    public List<RSAProofInfo> getRSAProofInformation(int transformatorID) {
        return buffers.values().stream()
                .map(Queue::peek)
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

    public ClientInfo[] getClientInfo(int transformatorID) {
        return buffers.values().stream()
                .map(Queue::peek)
                .filter(Objects::nonNull)
                .map(share -> new ClientInfo(share.getProofComponent()))
                .toArray(ClientInfo[]::new);
    }
}

