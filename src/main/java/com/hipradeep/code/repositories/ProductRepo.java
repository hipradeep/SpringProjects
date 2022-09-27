package com.hipradeep.code.repositories;

import com.hipradeep.code.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo  extends JpaRepository<Product, Integer> {
}
