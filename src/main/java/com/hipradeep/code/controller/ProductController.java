package com.hipradeep.code.controller;

import com.hipradeep.code.model.Product;
import com.hipradeep.code.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller exposing endpoints for creating and retrieving Products with custom Snowflake IDs.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;

    @Autowired
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Creates a new Product. The primary key ID is generated using the Snowflake algorithm.
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Map<String, Object> payload) {
        String name = (String) payload.getOrDefault("name", "Standard Item");
        double price = Double.parseDouble(payload.getOrDefault("price", "9.99").toString());

        Product product = new Product(name, price);
        Product savedProduct = productRepository.save(product);
        return ResponseEntity.ok(savedProduct);
    }

    /**
     * Retrieves a single Product by its custom Snowflake ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lists all Products currently registered in the database.
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }
}
