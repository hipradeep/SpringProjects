package com.hipradeep.code.controller;

import com.hipradeep.code.entity.Product;
import com.hipradeep.code.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ProductService service;

    @Test
    public void testSaveProduct() {
        Product inputProduct = new Product(null, "Mobile", 1, 15000.0);
        Product savedProduct = new Product(1, "Mobile", 1, 15000.0);
        when(service.saveProduct(any())).thenReturn(Mono.just(savedProduct));

        webTestClient.post()
                .uri("/products")
                .body(Mono.just(inputProduct), Product.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class)
                .consumeWith(result -> {
                    Product responseBody = result.getResponseBody();
                    assert responseBody != null;
                    assert responseBody.getId() != null;
                    assert responseBody.getId() == 1;
                });
    }

    @Test
    public void testGetProducts() {
        Product p1 = new Product(101, "Mobile", 1, 15000.0);
        Product p2 = new Product(102, "Laptop", 1, 55000.0);
        when(service.getProducts(any(Pageable.class))).thenReturn(Flux.just(p1, p2));

        webTestClient.get()
                .uri("/products")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Product.class)
                .hasSize(2)
                .contains(p1, p2);
    }

    @Test
    public void testGetProductById() {
        Product product = new Product(101, "Mobile", 1, 15000.0);
        when(service.getProductById(101)).thenReturn(Mono.just(product));

        webTestClient.get()
                .uri("/products/101")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class)
                .isEqualTo(product);
    }

    @Test
    public void testUpdateProduct() {
        Product product = new Product(101, "Mobile", 1, 15000.0);
        when(service.updateProduct(any(), eq(101))).thenReturn(Mono.just(product));

        webTestClient.put()
                .uri("/products/update/101")
                .body(Mono.just(product), Product.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void testDeleteProduct() {
        when(service.deleteProduct(101)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/products/delete/101")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void testGetProductByIdNotFound() {
        when(service.getProductById(999)).thenReturn(Mono
                .error(new com.hipradeep.code.exception.ProductNotFoundException("Product not found with id: 999")));

        webTestClient.get()
                .uri("/products/999")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errorMessage").isEqualTo("Product not found with id: 999");
    }

    @Test
    public void testGetProductInRange() {
        Product p1 = new Product(101, "Mobile", 1, 15000.0);
        Product p2 = new Product(102, "Laptop", 1, 55000.0);
        when(service.getProductInRange(10000.0, 60000.0)).thenReturn(Flux.just(p1, p2));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/products/product-range")
                        .queryParam("min", 10000.0)
                        .queryParam("max", 60000.0)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Product.class)
                .hasSize(2)
                .contains(p1, p2);
    }
}
