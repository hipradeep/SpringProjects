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
        // HttpClient introduced in Java 9 (standardized in Java 11)
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2) // Specifically supports HTTP/2
                .build();
        this.objectMapper = objectMapper;
    }

    public CompletableFuture<Album> findByIdAsync(Integer id) {
        log.info("Fetching album with id {} asynchronously using Java 9+ HttpClient", id);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/albums/" + id))
                .header("Accept", "application/json")
                .GET()
                .build();

        // sendAsync returns a CompletableFuture
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() == 200) {
                        try {
                            return objectMapper.readValue(response.body(), Album.class);
                        } catch (Exception e) {
                            throw new RuntimeException("Mapping error", e);
                        }
                    } else {
                        throw new RuntimeException("API error: " + response.statusCode());
                    }
                })
                .exceptionally(ex -> {
                    log.error("Failed to fetch album: {}", ex.getMessage());
                    return null;
                });
    }
}
