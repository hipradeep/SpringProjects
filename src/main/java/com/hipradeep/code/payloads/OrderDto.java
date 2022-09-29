package com.hipradeep.code.payloads;

import com.hipradeep.code.entities.OrderDetails;
import lombok.Data;

import java.util.Set;

@Data
public class OrderDto {


    private String customer;
    private String total;

    private Set<OrderDetailsDto> orderDetails;
}
