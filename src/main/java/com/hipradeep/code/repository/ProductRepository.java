package com.hipradeep.code.repository;

import com.hipradeep.code.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository extends R2dbcRepository<Product, Integer> {
    Flux<Product> findByPriceBetween(double min, double max);

    Flux<Product> findAllBy(Pageable pageable);
}
