package com.master_thesis.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
public class PublicParameters {
    private HttpAdapter httpAdapter;
    private int serverID;

    @Autowired
    public PublicParameters(HttpAdapter httpAdapter) throws InterruptedException {
        this.httpAdapter = httpAdapter;
        serverID = httpAdapter.registerServer();
    }

    public List<Integer> getClients(int transformatorID) {
        return httpAdapter.getClients(transformatorID);
    }

    public int getServerID() {
        return serverID;
    }

    public BigInteger getFieldBase(int transformatorID) {
        return httpAdapter.getFieldBase(transformatorID);
    }

    public BigInteger getGenerator(int transformatorID) {
        return httpAdapter.getGenerator(transformatorID);
    }
}
