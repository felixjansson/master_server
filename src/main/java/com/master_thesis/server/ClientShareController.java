package com.master_thesis.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping(value = "/api")
public class ClientShareController {
    private HashMap<Integer, Buffer> buffers;
    private PublicParameters publicParameters;
    private ServerSecretSharing secretSharing;
    private HttpAdapter httpAdapter;

    @Autowired
    public ClientShareController(PublicParameters publicParameters, ServerSecretSharing serverSecretSharing, HttpAdapter httpAdapter){
        this.buffers = new HashMap<>();
        this.publicParameters = publicParameters;
        this.secretSharing = serverSecretSharing;
        this.httpAdapter = httpAdapter;
    }

    @PostMapping(value = "/client-share")
    void ReceiveShare(@RequestBody ClientShare clientShare){
        put(clientShare);
        Buffer buffer = buffers.get(clientShare.getTransformatorID());
        if (buffer.canCompute()){
            System.out.println("Can compute");
            List<Integer> shares = buffer.getShares();
            PartialObject partialObject = new PartialObject();
            partialObject.setPartialResult(secretSharing.partialEval(shares));
            partialObject.setPartialProof(secretSharing.partialProof(clientShare.getProofComponent()));
            partialObject.setServerID(publicParameters.getServerID());
            URI uri = URI.create("http://localhost:3000/api/partials");
            httpAdapter.send(uri, partialObject);
        }

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
