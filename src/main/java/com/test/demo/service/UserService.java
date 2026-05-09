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
        // Step 1: Initialize the standard Java HttpClient.
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = objectMapper;
    }

    public User findUserById(Integer id) {
        log.info("Fetching user with id {} using HttpClient", id);
        try {
            // Step 2: Build the HTTP Request using the HttpRequest builder.
            HttpRequest request = HttpRequest.newBuilder()
                    // Step 3: Specify the full URI for the request.
                    .uri(URI.create("https://jsonplaceholder.typicode.com/users/" + id))
                    // Step 4: Add custom headers.
                    .header("X-Source", "Java-HttpClient")
                    .header("Accept", "application/json")
                    // Step 5: Specify the HTTP method (GET in this case).
                    .GET()
                    .build();

            // Step 6: Send the request synchronously. This blocks the thread until the response is received.
            // We use BodyHandlers.ofString() to get the response body as a String.
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Step 7: Check the HTTP status code.
            if (response.statusCode() == 200) {
                // Step 8: Manually map the JSON string response to our User object using Jackson's ObjectMapper.
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
