package com.hipradeep.code.services;

import com.hipradeep.code.payloads.OrderDto;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDto);
}
