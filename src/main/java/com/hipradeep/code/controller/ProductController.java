package com.hipradeep.code.controller;

import com.hipradeep.code.entity.Product;
import com.hipradeep.code.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping
    public Flux<Product> getProducts() {
        return service.getProducts();
    }

    @GetMapping("/{id}")
    public Mono<Product> getProductById(@PathVariable Integer id) {
        return service.getProductById(id);
    }

    @GetMapping("/product-range")
    public Flux<Product> getProductBetweenRange(@RequestParam("min") double min, @RequestParam("max") double max) {
        return service.getProductInRange(min, max);
    }

    @PostMapping
    public Mono<Product> saveProduct(@RequestBody Mono<Product> productMono) {
        return service.saveProduct(productMono);
    }

    @PutMapping("/update/{id}")
    public Mono<Product> updateProduct(@RequestBody Mono<Product> productMono, @PathVariable Integer id) {
        return service.updateProduct(productMono, id);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<Void> deleteProduct(@PathVariable Integer id) {
        return service.deleteProduct(id);
    }
}
