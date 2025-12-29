package com.hipradeep.code.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/api/reactive")
public class ReactiveDemoController {

    /**
     * Mono represents a stream of 0 or 1 element.
     * Use it for single asynchronous values like a database lookup for a single
     * user.
     */
    @GetMapping("/mono")
    public Mono<String> getMono() {
        return Mono.just("Hello from Mono!")
                .log(); // log() helps in debugging reactive streams
    }

    /**
     * Flux represents a stream of 0 to N elements.
     * Use it for sequences of values, like a list of items or a real-time data
     * feed.
     */
    @GetMapping("/flux")
    public Flux<Integer> getFlux() {
        return Flux.just(1, 2, 3, 4, 5)
                .delayElements(Duration.ofMillis(100))
                .log();
    }

    /**
     * Streaming Flux with media type text/event-stream or application/x-ndjson.
     * This allows the browser/client to process elements as they arrive.
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getStream() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(i -> "Event " + i)
                .take(10) // Limit to 10 events
                .log();
    }
}
