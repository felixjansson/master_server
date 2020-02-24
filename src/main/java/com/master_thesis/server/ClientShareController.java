package com.master_thesis.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

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
        int transformatorID = clientShare.getTransformatorID();
        buffers.putIfAbsent(transformatorID, new Buffer(publicParameters, transformatorID));
        Buffer buffer = buffers.get(transformatorID);
        buffer.putClientShare(clientShare);
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
}
