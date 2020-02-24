package com.master_thesis.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class PublicParameters {
    private HttpAdapter httpAdapter;
    private List<Integer> clients;
    private int serverID;

    @Autowired
    public PublicParameters(HttpAdapter httpAdapter) {
        this.httpAdapter = httpAdapter;
        serverID = httpAdapter.registerServer();
        clients = httpAdapter.getClients();
    }

    public List<Integer> getClients(){
        return clients;
    }

    public int getServerID() {
        return serverID;
    }
}
