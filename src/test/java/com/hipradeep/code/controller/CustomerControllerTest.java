package com.hipradeep.code.controller;

import com.hipradeep.code.dto.Customer;
import com.hipradeep.code.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.Arrays;

import static org.mockito.Mockito.when;

@WebFluxTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CustomerService service;

    @Test
    public void testGetAllCustomers() {
        Customer c1 = new Customer(1, "customer1");
        Customer c2 = new Customer(2, "customer2");
        when(service.loadAllCustomers()).thenReturn(Arrays.asList(c1, c2));

        webTestClient.get()
                .uri("/customers")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Customer.class)
                .hasSize(2)
                .contains(c1, c2);
    }

    @Test
    public void testGetAllCustomersStream() {
        Customer c1 = new Customer(1, "customer1");
        Customer c2 = new Customer(2, "customer2");
        when(service.loadAllCustomersStream()).thenReturn(Flux.just(c1, c2));

        webTestClient.get()
                .uri("/customers/stream")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
                .expectBodyList(Customer.class)
                .hasSize(2)
                .contains(c1, c2);
    }
}
