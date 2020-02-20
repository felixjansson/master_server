package com.master_thesis.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping(value = "/api")
public class ClientShareController {
    private HashMap<Integer, Buffer> buffers;
    private PublicParameters publicParameters;

    @Autowired
    public ClientShareController(PublicParameters publicParameters){
        this.buffers = new HashMap<>();
        this.publicParameters = publicParameters;
    }

    @PostMapping(value = "/client-share")
    void ReceiveShare(@RequestBody ClientShare clientShare){
        put(clientShare);
        System.out.println(clientShare.getClientID());
    }

    private void put(ClientShare clientShare){
        if (clientShare == null){
            System.out.println("Error: ClientShare is Null");
            return;
        }
        int transformatorID = clientShare.getTransformatorID();
        Buffer buffer = buffers.get(transformatorID);

        if (buffer == null){
            List<Integer> clients = publicParameters.getClients();
            Buffer newBuffer = new Buffer(transformatorID, clients.size());
            newBuffer.putClientShare(clientShare);
            buffers.put(transformatorID, newBuffer);
        } else {
            buffer.putClientShare(clientShare);
        }





    }

}
