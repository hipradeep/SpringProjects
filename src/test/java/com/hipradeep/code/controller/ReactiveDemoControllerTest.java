package com.hipradeep.code.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

@WebFluxTest(ReactiveDemoController.class)
public class ReactiveDemoControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testGetMono() {
        webTestClient.get()
                .uri("/api/reactive/mono")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Hello from Mono!");
    }

    @Test
    public void testGetFlux() {
        webTestClient.get()
                .uri("/api/reactive/flux")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .hasSize(5)
                .contains(1, 2, 3, 4, 5);
    }

    @Test
    public void testGetStream() {
        webTestClient.get()
                .uri("/api/reactive/stream")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM);

        // Note: For streaming, StepVerifier is often used if we want to check
        // individual elements over time
        // but WebTestClient's expectBodyList or consumeWith can also work for bounded
        // streams.
    }
}
