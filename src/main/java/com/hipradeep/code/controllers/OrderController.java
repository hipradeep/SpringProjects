package com.hipradeep.code.controllers;

import com.hipradeep.code.payloads.ApiResponse;
import com.hipradeep.code.payloads.OrderDto;
import com.hipradeep.code.payloads.ProductDto;
import com.hipradeep.code.services.OrderService;
import com.hipradeep.code.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/")
    public ResponseEntity<ApiResponse<OrderDto>> createOrder(@Valid @RequestBody OrderDto orderDto) throws Exception {

        OrderDto order= null;
        try {
            order = this.orderService.createOrder(orderDto);
        } catch (Exception e) {
            return new ResponseEntity<ApiResponse<OrderDto>>(new ApiResponse<>(e.getMessage(), false), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new ApiResponse<>("Order created successfully!", true, order), HttpStatus.CREATED);

    }



}
