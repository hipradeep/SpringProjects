package com.hipradeep.code.services.impl;

import com.hipradeep.code.entities.Order;
import com.hipradeep.code.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrder(Order order) {
        System.out.println("[SERVICE] Creating new order...");
        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrder(UUID id, Double newPrice) {
        System.out.println("[SERVICE] Updating order...");
        Order order = orderRepository.findById(id).orElseThrow();
        order.setPrice(newPrice);
        return order;
    }

    @Transactional
    public void deleteOrder(UUID id) {
        System.out.println("[SERVICE] Deleting order...");
        orderRepository.deleteById(id);
    }

    public Order getOrder(UUID id) {
        System.out.println("[SERVICE] Fetching order...");
        return orderRepository.findById(id).orElseThrow();
    }
}
