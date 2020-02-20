package com.master_thesis.server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Buffer {

    private class ClientBuffer{
        Queue<Integer> queue;
        int id;

        public ClientBuffer(int id) {
            this.queue = new LinkedList<>();
            this.id = id;
        }
    }

    private HashMap<Integer, ClientBuffer> buffers;
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
        ClientBuffer clientBuffer = buffers.get(clientID);

        if (clientBuffer == null) {
            ClientBuffer newClientBuffer = new ClientBuffer(clientID);
            newClientBuffer.queue.add(clientShare.getShare());
            buffers.put(clientID, newClientBuffer);
        } else {
            clientBuffer.queue.add(clientShare.getShare());
        }
    }

    public int getTransformatorID() {
        return transformatorID;
    }
}

