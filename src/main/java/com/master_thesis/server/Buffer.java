package com.master_thesis.server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class Buffer {

    private HashMap<Integer, Queue<Integer>> buffers;
    private int transformatorID;
    private int expectedBufferSize;

    public Buffer(int transformationID, int expectedBufferSize) {
        this.transformatorID = transformationID;
        this.expectedBufferSize = expectedBufferSize;
        buffers = new HashMap<>();
    }

    public void putClientShare(ClientShare clientShare){
        if (clientShare == null) return;
        int clientID = clientShare.getClientID();
        Queue<Integer> clientBuffer = buffers.get(clientID);

        if (clientBuffer == null) {
            Queue<Integer> newQueue = new LinkedList<>();
            newQueue.add(clientShare.getShare());
            buffers.put(clientID, newQueue);
        } else {

            buffers.get(clientID).add(clientShare.getShare());
        }
    }

    public List<Integer> getShares(){
        if (!canCompute()) return null;
        return buffers.values().stream().map(Queue::poll).collect(Collectors.toList());
    }

    public int getTransformatorID() {
        return transformatorID;
    }

    public boolean canCompute(){ // TODO: 2020-02-24 Check with PP how many clients
        boolean allResponded = buffers.values().size() == expectedBufferSize;
        boolean noEmptyQueues = buffers.values().stream().noneMatch(Queue::isEmpty);
        return allResponded && noEmptyQueues;
    }
}

