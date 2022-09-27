package com.hipradeep.code.services.serviceImple;

import com.hipradeep.code.entities.Order;
import com.hipradeep.code.entities.Product;
import com.hipradeep.code.payloads.OrderDto;
import com.hipradeep.code.payloads.ProductDto;
import com.hipradeep.code.repositories.OrderRepo;
import com.hipradeep.code.repositories.ProductRepo;
import com.hipradeep.code.services.OrderService;
import org.aspectj.weaver.ast.Or;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepo orderRepo;

    @Autowired(required = true)
    private ModelMapper modelMapper;

    @Override
    public OrderDto createOrder(OrderDto orderDto) {

        Order order=this.orderRepo.save(this.modelMapper.map(orderDto, Order.class));

        return this.modelMapper.map(order, OrderDto.class);
    }
}
