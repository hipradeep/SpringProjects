package com.test.demo.service;

import com.test.demo.model.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class CommentService {

    private static final Logger log = LoggerFactory.getLogger(CommentService.class);
    private final WebClient webClient;

    public CommentService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://jsonplaceholder.typicode.com").build();
    }

    public List<Comment> findAllSync() {
        log.info("Fetching all comments synchronously using WebClient.block()");
        return webClient.get()
                .uri("/comments")
                .retrieve()
                .bodyToFlux(Comment.class)
                .collectList()
                .block(); // Blocking for demo purposes
    }

    public Mono<Comment> findByIdAsync(Integer id) {
        log.info("Fetching comment with id {} asynchronously using WebClient", id);
        return webClient.get()
                .uri("/comments/{id}", id)
                .retrieve()
                .bodyToMono(Comment.class);
    }
}
