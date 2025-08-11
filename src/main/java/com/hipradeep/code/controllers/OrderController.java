package com.hipradeep.code.controllers;

import com.hipradeep.code.entities.Order;
import com.hipradeep.code.services.impl.EntityStateDetector;
import com.hipradeep.code.services.impl.OrderService;
import com.hipradeep.code.services.impl.OrderStateService;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderStateService stateService;


    public OrderController(OrderService orderService, OrderStateService stateService) {
        this.orderService = orderService;
        this.stateService = stateService;
    }

    @PostMapping
    public Order create(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    @PutMapping("/{id}")
    public Order update(@PathVariable UUID id, @RequestParam Double price) {
        return orderService.updateOrder(id, price);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        orderService.deleteOrder(id);
    }

    @GetMapping("/{id}")
    public Order get(@PathVariable UUID id) {
        return orderService.getOrder(id);
    }


    @GetMapping("/demo-states")
    public String demonstrateStates() {
        stateService.demonstrateStates();
        return "Check console for state transition output";
    }
    @GetMapping("/demo-states2")
    public String demonstrateStates2() {
        //stateService.deleteOrderFromDatabase();
        return "Check console for state transition output";
    }
}
