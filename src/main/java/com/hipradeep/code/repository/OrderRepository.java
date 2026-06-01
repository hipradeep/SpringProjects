package com.hipradeep.code.repository;

import com.hipradeep.code.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Order records.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
}
