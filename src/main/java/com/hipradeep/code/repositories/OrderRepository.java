package com.hipradeep.code.repositories;

import com.hipradeep.code.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
