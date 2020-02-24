package com.master_thesis.server;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DummyServerSecretSharing implements ServerSecretSharing {

    @Override
    @SneakyThrows
    public int partialEval(List<Integer> shares) {
        return shares.stream().reduce(0,Integer::sum);
    }

    @Override
    public int partialProof(int proofComponent) {
        return proofComponent + 1;
    }
}
