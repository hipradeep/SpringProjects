package com.hipradeep.code.service;

import com.hipradeep.code.entity.Product;
import com.hipradeep.code.entity.Tag;
import com.hipradeep.code.repository.ProductRepository;
import com.hipradeep.code.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
// Removed Range import
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Collections;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private TagRepository tagRepository;

    public Flux<Product> getProducts() {
        return repository.findAll()
                .flatMap(product -> tagRepository.findByProductId(product.getId())
                        .collectList()
                        .doOnNext(product::setTags)
                        .thenReturn(product));
    }

    public Flux<Product> getProducts(Pageable pageable) {
        return repository.findAllBy(pageable)
                .flatMap(product -> tagRepository.findByProductId(product.getId())
                        .collectList()
                        .doOnNext(product::setTags)
                        .thenReturn(product));
    }

    public Mono<Product> getProductById(Integer id) {
        return repository.findById(id)
                .flatMap(product -> tagRepository.findByProductId(product.getId())
                        .collectList()
                        .doOnNext(product::setTags)
                        .thenReturn(product));
    }

    public Flux<Product> getProductInRange(double min, double max) {
        return repository.findByPriceBetween(min, max)
                .flatMap(product -> tagRepository.findByProductId(product.getId())
                        .collectList()
                        .doOnNext(product::setTags)
                        .thenReturn(product));
    }

    public Mono<Product> saveProduct(Mono<Product> productMono) {
        return productMono.flatMap(product -> repository.save(product)
                .flatMap(savedProduct -> {
                    if (product.getTags() != null && !product.getTags().isEmpty()) {
                        product.getTags().forEach(tag -> tag.setProductId(savedProduct.getId()));
                        return tagRepository.saveAll(product.getTags())
                                .collectList()
                                .doOnNext(savedProduct::setTags)
                                .thenReturn(savedProduct);
                    }
                    return Mono.just(savedProduct);
                }));
    }

    public Mono<Product> updateProduct(Mono<Product> productMono, Integer id) {
        return repository.findById(id)
                .flatMap(existingProduct -> productMono.flatMap(product -> {
                    product.setId(id);
                    return repository.save(product)
                            .flatMap(savedProduct -> {
                                // Delete old tags and save new ones from the 'product' request object
                                return tagRepository.findByProductId(id)
                                        .flatMap(tag -> tagRepository.delete(tag))
                                        .thenMany(Flux.fromIterable(product.getTags() != null ? product.getTags()
                                                : Collections.emptyList()))
                                        .doOnNext(tag -> {
                                            tag.setProductId(id);
                                            tag.setId(null); // Ensure new IDs are generated
                                        })
                                        .flatMap(tagRepository::save)
                                        .collectList()
                                        .doOnNext(savedProduct::setTags)
                                        .thenReturn(savedProduct);
                            });
                }));
    }

    public Mono<Void> deleteProduct(Integer id) {
        return repository.deleteById(id);
    }
}
