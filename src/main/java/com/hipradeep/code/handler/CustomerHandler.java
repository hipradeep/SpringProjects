package com.hipradeep.code.handler;

import com.hipradeep.code.dao.CustomerDao;
import com.hipradeep.code.dto.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerHandler {

    @Autowired
    private CustomerDao dao;

    public Mono<ServerResponse> loadCustomers(ServerRequest request) {
        Flux<Customer> customerList = Flux.fromIterable(dao.getCustomers());
        return ServerResponse.ok().body(customerList, Customer.class);
    }

    public Mono<ServerResponse> findCustomer(ServerRequest request) {
        int customerId = Integer.parseInt(request.pathVariable("input"));
        // Mono<Customer> customerMono = dao.getCustomers().stream().filter(c ->
        // c.getId() == customerId).findFirst().map(Mono::just).orElse(Mono.empty());
        // For simplicity, just return a dummy search result based on ID
        return ServerResponse.ok().body(Mono.just(new Customer(customerId, "customer" + customerId)), Customer.class);
    }

    public Mono<ServerResponse> saveCustomer(ServerRequest request) {
        Mono<Customer> customerMono = request.bodyToMono(Customer.class);
        Mono<String> responseMono = customerMono.map(dto -> dto.getId() + ":" + dto.getName());
        return ServerResponse.ok().body(responseMono, String.class);
    }
}
