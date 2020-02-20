package com.master_thesis.server;

import com.fasterxml.jackson.annotation.JsonAlias;

public class ClientShare {

    private int share;
    private int clientID;
    private int transformatorID;

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public int getTransformatorID() {
        return transformatorID;
    }

    public void setTransformatorID(int transformatorID) {
        this.transformatorID = transformatorID;
    }
}
