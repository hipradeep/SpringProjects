package com.hipradeep.code.repository;

import com.hipradeep.code.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Product entities.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
