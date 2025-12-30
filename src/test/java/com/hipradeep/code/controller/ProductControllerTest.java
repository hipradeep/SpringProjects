package com.hipradeep.code.controller;

import com.hipradeep.code.dto.ProductDto;
import com.hipradeep.code.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
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
        ProductDto productDto = new ProductDto("101", "Mobile", 1, 15000.0);
        when(service.saveProduct(any())).thenReturn(Mono.just(productDto));

        webTestClient.post()
                .uri("/products")
                .body(Mono.just(productDto), ProductDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductDto.class)
                .isEqualTo(productDto);
    }

    @Test
    public void testGetProducts() {
        ProductDto p1 = new ProductDto("101", "Mobile", 1, 15000.0);
        ProductDto p2 = new ProductDto("102", "Laptop", 1, 55000.0);
        when(service.getProducts()).thenReturn(Flux.just(p1, p2));

        webTestClient.get()
                .uri("/products")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductDto.class)
                .hasSize(2)
                .contains(p1, p2);
    }

    @Test
    public void testGetProductById() {
        ProductDto productDto = new ProductDto("101", "Mobile", 1, 15000.0);
        when(service.getProductById("101")).thenReturn(Mono.just(productDto));

        webTestClient.get()
                .uri("/products/101")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductDto.class)
                .isEqualTo(productDto);
    }

    @Test
    public void testUpdateProduct() {
        ProductDto productDto = new ProductDto("101", "Mobile", 1, 15000.0);
        when(service.updateProduct(any(), eq("101"))).thenReturn(Mono.just(productDto));

        webTestClient.put()
                .uri("/products/update/101")
                .body(Mono.just(productDto), ProductDto.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void testDeleteProduct() {
        when(service.deleteProduct("101")).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/products/delete/101")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void testGetProductInRange() {
        ProductDto p1 = new ProductDto("101", "Mobile", 1, 15000.0);
        ProductDto p2 = new ProductDto("102", "Laptop", 1, 55000.0);
        when(service.getProductInRange(10000.0, 60000.0)).thenReturn(Flux.just(p1, p2));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/products/product-range")
                        .queryParam("min", 10000.0)
                        .queryParam("max", 60000.0)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductDto.class)
                .hasSize(2)
                .contains(p1, p2);
    }
}
