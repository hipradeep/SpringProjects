package com.hipradeep.code;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MonoFluxTest {

    @Test
    public void testMono() {
        // Mono<?> represents 0 or 1 element.
        // then() will switch to the provided Mono after completion of the first one.
        Mono<?> monoString = Mono.just("javatechie")
                .then(Mono.error(new RuntimeException("Exception occured")))
                .log();

        // Subscribing and providing consumer for value and error
        monoString.subscribe(System.out::println, (e) -> System.out.println(e.getMessage()));
    }

    @Test
    public void testFlux() {
        // Flux<String> represents 0 to N elements.
        // concatWithValues and concatWith add elements to the stream sequentially.
        // Note: Elements after an error will not be emitted.
        Flux<String> fluxString = Flux.just("Spring", "Spring Boot", "Hibernate")
                .concatWithValues("AWS")
                .concatWith(Flux.error(new RuntimeException("Exception occured")))
                .concatWithValues("cloud")
                .log();

        fluxString.subscribe(System.out::println, (e) -> System.out.println(e.getMessage()));
    }
}
