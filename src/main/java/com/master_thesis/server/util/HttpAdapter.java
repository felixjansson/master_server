package com.master_thesis.server.util;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Component
public class HttpAdapter {

    private static final Logger log = (Logger) LoggerFactory.getLogger(HttpAdapter.class);
    private final String coordinator;
    private ObjectMapper objectMapper;
    private String serverURI;


    @Autowired
    public HttpAdapter(ObjectMapper objectMapper, Environment environment) {
        this.objectMapper = objectMapper;
        String port = environment.getProperty("server.port");
        serverURI = "{\"uri\":\"http://localhost:" + port + "/api\"}";
        coordinator = "http://localhost:4000/api/%s";
    }

    public void send(URI uri, Object information) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(uri)
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(information))).build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        log.debug("To {}: {}", uri, objectMapper.writeValueAsString(information));
        if (!response.body().isEmpty()) {
            log.debug("Got answer: {}", response.body());
        }
    }

    public void sendWithTimeout(URI uri, Object information, int timeoutMs) {
        boolean sendFailed = true;
        int tries = 10;
        while (sendFailed) {
            try {
                send(uri, information);
                sendFailed = false;
            } catch (InterruptedException | IOException e) {
                log.error("Sending did not work, sleeping for {}ms. {}", timeoutMs, e.getMessage());
                sendFailed = --tries > 0;
                try {
                    Thread.sleep(timeoutMs);
                } catch (InterruptedException ex) {
                    log.error("Could not sleep while trying to send message. {}", ex.getMessage());
                }
            }
        }
    }

    public int registerServer() throws InterruptedException {
        URI uri = URI.create(String.format(coordinator, "server/register"));
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(serverURI);
            HttpRequest request = HttpRequest.newBuilder(uri)
                    .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonNode.toString())).build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

//            return serverID
            return objectMapper.readValue(response.body(), Integer.class);
        } catch (IOException | InterruptedException e) {
            int sleepTime = 1000;
            log.error("Error in registering server, sleeping for {}ms. {}", sleepTime, e.getMessage());
            Thread.sleep(sleepTime);
            return registerServer();
        }

    }

    @SneakyThrows
    public List<Integer> getClients(int substationID) {
        URI uri = URI.create(String.format(coordinator, "client/list/" + substationID));
        HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), List.class);
    }

    @SneakyThrows
    public List<Integer> getClients(int substationID, int fid) {
        URI uri = URI.create(String.format(coordinator, "client/list/" + substationID + "/" + fid));
        HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), List.class);
    }

    @SneakyThrows
    public BigInteger getFieldBase(int substationID) {
        URI uri = URI.create("http://localhost:4000/api/setup/fieldBase/" + substationID);
        HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return new BigInteger(response.body());
    }

    @SneakyThrows
    public BigInteger getGenerator(int substationID) {
        URI uri = URI.create("http://localhost:4000/api/setup/generator/" + substationID);
        HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return new BigInteger(response.body());
    }

    @SneakyThrows
    public String getServerList() {
        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create("http://localhost:4000/api/server/list"))
                .GET().build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
