package com.hipradeep.code.payloads;

import com.hipradeep.code.entities.Order;
import com.hipradeep.code.entities.Product;
import lombok.Data;

import java.util.Set;

@Data
public class OrderDetailsDto {

    private long id;
    private String qty;
    private String subTotal;
    private Set<ProductDto> product;
    private OrderDto order;

}
