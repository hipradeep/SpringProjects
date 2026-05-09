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
        // Step 1: Build the WebClient using the injected WebClient.Builder.
        // The builder helps apply global configurations like base URLs.
        this.webClient = webClientBuilder.baseUrl("https://jsonplaceholder.typicode.com").build();
    }

    public List<Comment> findAllSync() {
        log.info("Fetching all comments synchronously using WebClient.block()");
        // Step 2: Use WebClient to make a GET request.
        return webClient.get()
                // Step 3: Append the specific URI path.
                .uri("/comments")
                // Step 4: Perform the request.
                .retrieve()
                // Step 5: Convert the response body to a Flux (a reactive stream of Comment objects).
                .bodyToFlux(Comment.class)
                // Step 6: Collect all the elements in the stream into a single List.
                .collectList()
                // Step 7: Block the thread to wait for the result. (Use with caution in reactive apps).
                .block(); 
    }

    public Mono<Comment> findByIdAsync(Integer id) {
        log.info("Fetching comment with id {} asynchronously using WebClient", id);
        // Step 2: Use WebClient to make a GET request.
        return webClient.get()
                // Step 3: Append the specific URI path, injecting the path variable.
                .uri("/comments/{id}", id)
                // Step 4: Perform the request.
                .retrieve()
                // Step 5: Convert the response body to a Mono (a reactive publisher that emits 0 or 1 item).
                // This returns immediately without blocking.
                .bodyToMono(Comment.class);
    }
}
