package com.hipradeep.code;

import com.hipradeep.code.entity.Product;

import com.hipradeep.code.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.ArgumentMatchers.anyInt;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureWebTestClient
public class ProductHandlerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ProductService productService;

    @Test
    public void testGetAllProducts() {
        Flux<Product> productFlux = Flux.just(
                new Product(101, "Mobile", 1, 10000),
                new Product(102, "TV", 1, 20000));

        when(productService.getProducts()).thenReturn(productFlux);

        webTestClient.get().uri("/router/products")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Product.class)
                .hasSize(2);
    }

    @Test
    public void testGetProductById_Success() {
        Product product = new Product(101, "Mobile", 1, 10000);
        when(productService.getProductById(anyInt())).thenReturn(Mono.just(product));

        webTestClient.get().uri("/router/products/101")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class)
                .isEqualTo(product);
    }

    @Test
    public void testGetProductById_NotFound() {
        // Mocking the service to throw the specific exception we want to test global
        // handling for
        // However, the Service itself throws the exception, so we mimic that behavior
        when(productService.getProductById(anyInt()))
                .thenReturn(Mono.error(new com.hipradeep.code.exception.ProductNotFoundException("Product not found")));

        webTestClient.get().uri("/router/products/999")
                .exchange()
                .expectStatus().isNotFound() // Expecting 404 from GlobalExceptionHandler
                .expectBody()
                .jsonPath("$.message").isEqualTo("Product not found")
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.error").isEqualTo("Product Not Found");
    }

    @Test
    public void testSaveProduct() {
        Product product = new Product(101, "Mobile", 1, 10000);
        when(productService.saveProduct(any())).thenReturn(Mono.just(product));

        webTestClient.post().uri("/router/products/save")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(product), Product.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class)
                .isEqualTo(product);
    }
}
