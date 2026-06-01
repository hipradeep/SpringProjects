package com.hipradeep.code.controller;

import com.hipradeep.code.model.Order;
import com.hipradeep.code.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller exposing endpoints for creating and retrieving Orders.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Creates a new Order. The primary key ID is auto-assigned via custom OrderIdGenerator.
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Map<String, Object> payload) {
        Long itemId = Long.parseLong(payload.getOrDefault("itemId", "9876543210").toString());
        String customerName = (String) payload.getOrDefault("customerName", "Guest Customer");
        int quantity = Integer.parseInt(payload.getOrDefault("quantity", "1").toString());

        Order order = new Order(itemId, customerName, quantity);
        Order savedOrder = orderRepository.save(order);
        return ResponseEntity.ok(savedOrder);
    }

    /**
     * Retrieves a single Order by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable String id) {
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lists all Orders.
     */
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderRepository.findAll());
    }
}
