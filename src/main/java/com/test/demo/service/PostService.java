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
        this.restClient = RestClient.builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .defaultHeader("X-App-Name", "Demo-Application")
                .build();
    }

    public List<Post> findAll() {
        log.info("Fetching all posts...");
        return restClient.get()
                .uri("/posts")
                .header("X-Source", "Spring-RestClient")
                .retrieve()
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
