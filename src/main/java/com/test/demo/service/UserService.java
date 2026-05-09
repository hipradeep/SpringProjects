package com.test.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.demo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public UserService(ObjectMapper objectMapper) {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = objectMapper;
    }

    public User findUserById(Integer id) {
        log.info("Fetching user with id {} using HttpClient", id);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://jsonplaceholder.typicode.com/users/" + id))
                    .header("X-Source", "Java-HttpClient")
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), User.class);
            } else {
                log.error("Failed to fetch user. Status code: {}", response.statusCode());
                return null;
            }
        } catch (Exception e) {
            log.error("Error while fetching user with id {}: {}", id, e.getMessage());
            throw new RuntimeException("External API error", e);
        }
    }
}
