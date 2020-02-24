package com.master_thesis.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Component
public class HttpAdapter {

    private ObjectMapper objectMapper;
    private String serverURI;
    private Environment environment;

    @Autowired
    public HttpAdapter(ObjectMapper objectMapper, Environment environment) {
        this.objectMapper = objectMapper;
        this.environment = environment;
        String port = environment.getProperty("server.port");
        StringBuilder sb = new StringBuilder("{\"uri\":\"http://localhost:");
        sb.append(port);
        sb.append("/api/client-share\"}");
        serverURI = sb.toString();


    }

    @SneakyThrows
    public void send(URI uri, PartialObject information){
        HttpRequest request = HttpRequest.newBuilder(uri)
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(information))).build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    @SneakyThrows
    public int registerServer( ){
        URI uri = URI.create("http://localhost:4000/api/server/register");
        JsonNode jsonNode = objectMapper.readTree(serverURI);
        HttpRequest request = HttpRequest.newBuilder(uri)
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonNode.toString())).build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        Integer serverID = objectMapper.readValue(response.body(),Integer.class);
        return serverID;
    }

    @SneakyThrows
    public List<Integer> getClients(int transformatorID){
        URI uri = URI.create("http://localhost:4000/api/client/list/" + transformatorID);
        HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), List.class);
    }

}
