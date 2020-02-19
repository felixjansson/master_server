package com.master_thesis.server;

public class ClientShare {

    private int share;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getShare() {
        return share;
    }
    public void setShare(int share) {
        this.share = share;
    }

    @Override
    public String toString(){
        return "Client id: " + id + ", Share: " + share;
    }
}
