package com.test.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.demo.model.Album;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@Service
public class AlbumService {

    private static final Logger log = LoggerFactory.getLogger(AlbumService.class);
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public AlbumService(ObjectMapper objectMapper) {
        // Step 1: Initialize the HttpClient using the builder.
        // The HttpClient was introduced in Java 9 (standardized in Java 11).
        this.httpClient = HttpClient.newBuilder()
                // Step 2: Explicitly set the HTTP version (optional, but good for demonstrating features).
                .version(HttpClient.Version.HTTP_2) // Specifically supports HTTP/2
                .build();
        this.objectMapper = objectMapper;
    }

    public CompletableFuture<Album> findByIdAsync(Integer id) {
        log.info("Fetching album with id {} asynchronously using Java 9+ HttpClient", id);
        
        // Step 3: Build the HTTP Request.
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/albums/" + id))
                .header("Accept", "application/json")
                .GET()
                .build();

        // Step 4: Use sendAsync to send the request without blocking the current thread.
        // It returns a CompletableFuture immediately.
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                // Step 5: Once the response arrives, process it asynchronously using thenApply.
                .thenApply(response -> {
                    // Step 6: Check the status code.
                    if (response.statusCode() == 200) {
                        try {
                            // Step 7: Map the JSON response to the Album object.
                            return objectMapper.readValue(response.body(), Album.class);
                        } catch (Exception e) {
                            throw new RuntimeException("Mapping error", e);
                        }
                    } else {
                        throw new RuntimeException("API error: " + response.statusCode());
                    }
                })
                // Step 8: Handle any exceptions that occurred during the request or mapping.
                .exceptionally(ex -> {
                    log.error("Failed to fetch album: {}", ex.getMessage());
                    return null;
                });
    }
}
