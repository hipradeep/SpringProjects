package com.hipradeep.code.controller;

import com.hipradeep.code.service.StreamingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@WebFluxTest(StreamingController.class)
public class StreamingControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private StreamingService service;

    @Test
    public void testStreamVideo() {
        // Mock a dummy video resource
        Resource mockVideo = new ByteArrayResource("dummy video content".getBytes());
        when(service.getVideoMp4("test")).thenReturn(Mono.just(mockVideo));

        webTestClient.get()
                .uri("/stream/test")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("video/x-matroska")
                .expectBody(byte[].class).isEqualTo("dummy video content".getBytes());
    }

    @Test
    public void testStreamVideoMp4() {
        // Mock a dummy video resource
        Resource mockVideo = new ByteArrayResource("dummy mp4 content".getBytes());
        when(service.getVideoMp4("test")).thenReturn(Mono.just(mockVideo));

        webTestClient.get()
                .uri("/stream/mp4/test")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("video/mp4")
                .expectBody(byte[].class).isEqualTo("dummy mp4 content".getBytes());
    }
}
