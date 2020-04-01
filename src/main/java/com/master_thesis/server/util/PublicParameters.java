package com.master_thesis.server.util;

import lombok.SneakyThrows;
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

    @SneakyThrows
    public void reRegisterServer() {
        serverID = httpAdapter.registerServer();
    }

    public List<Integer> getClients(int substationID) {
        return httpAdapter.getClients(substationID);
    }

    public List<Integer> getClients(int substationID, int fid) {
        return httpAdapter.getClients(substationID, fid);
    }

    public int getServerID() {
        return serverID;
    }

    public BigInteger getFieldBase(int substationID) {
        return httpAdapter.getFieldBase(substationID);
    }

    public BigInteger getGenerator(int substationID) {
        return httpAdapter.getGenerator(substationID);
    }
}
