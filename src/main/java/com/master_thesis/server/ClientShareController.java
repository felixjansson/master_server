package com.master_thesis.server;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientShareController {

    @PostMapping(value = "/api/client-share")
    void ReceiveShare(@RequestBody ClientShare share){
        System.out.println(share.toString());
    }


}
