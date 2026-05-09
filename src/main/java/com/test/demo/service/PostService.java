package com.test.demo.service;

import com.test.demo.model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class PostService {

    private static final Logger log = LoggerFactory.getLogger(PostService.class);
    private final RestClient restClient;

    public PostService() {
        log.info("Initializing RestClient with base URL: https://jsonplaceholder.typicode.com");
        // Step 1: Initialize the RestClient using its builder. 
        // We set a base URL so we don't have to repeat it in every request.
        // We also set a default header that will be applied to all requests.
        this.restClient = RestClient.builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .defaultHeader("X-App-Name", "Demo-Application")
                .build();
    }

    public List<Post> findAll() {
        log.info("Fetching all posts...");
        // Step 2: Use the initialized RestClient to perform a GET request.
        return restClient.get()
                // Step 3: Append the specific URI path for this endpoint.
                .uri("/posts")
                // Step 4: Add any request-specific headers.
                .header("X-Source", "Spring-RestClient")
                // Step 5: Execute the request and retrieve the response.
                .retrieve()
                // Step 6: Convert the JSON response body into a List of Post objects.
                .body(new ParameterizedTypeReference<List<Post>>() {});
    }

    public Post findById(Integer id) {
        log.info("Fetching post with id: {}", id);
        return restClient.get()
                .uri("/posts/{id}", id)
                .retrieve()
                .body(Post.class);
    }
}
