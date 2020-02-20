package com.master_thesis.server;

import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class PublicParameters {
    private LinkedList<Integer> clients;

    public PublicParameters() {
        // Dummy implementation to mimic two clients
        clients = new LinkedList<>();
        clients.add(1);
        clients.add(2);
    }

    List<Integer> getClients(){
        return clients;
    }
}
