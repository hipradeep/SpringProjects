package com.hipradeep.code.repository;

import com.hipradeep.code.entity.Tag;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TagRepository extends R2dbcRepository<Tag, Integer> {
    Flux<Tag> findByProductId(Integer productId);
}
