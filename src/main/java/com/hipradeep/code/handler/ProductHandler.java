package com.hipradeep.code.handler;

import com.hipradeep.code.entity.Product;
import com.hipradeep.code.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ProductHandler {

    @Autowired
    private ProductService service;

    public Mono<ServerResponse> getProducts(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.getProducts(), Product.class);
    }

    public Mono<ServerResponse> getProductById(ServerRequest request) {
        int id = Integer.parseInt(request.pathVariable("id"));
        return service.getProductById(id)
                .flatMap(product -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(product), Product.class));
    }

    public Mono<ServerResponse> saveProduct(ServerRequest request) {
        Mono<Product> productMono = request.bodyToMono(Product.class);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.saveProduct(productMono), Product.class);
    }

    public Mono<ServerResponse> updateProduct(ServerRequest request) {
        int id = Integer.parseInt(request.pathVariable("id"));
        Mono<Product> productMono = request.bodyToMono(Product.class);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.updateProduct(productMono, id), Product.class);
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest request) {
        int id = Integer.parseInt(request.pathVariable("id"));
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.deleteProduct(id), Void.class);
    }

    public Mono<ServerResponse> getProductInRange(ServerRequest request) {
        double min = Double.parseDouble(request.queryParam("min").orElse("0"));
        double max = Double.parseDouble(request.queryParam("max").orElse("0"));
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.getProductInRange(min, max), Product.class);
    }
}
