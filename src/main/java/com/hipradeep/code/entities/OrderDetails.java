package com.hipradeep.code.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "order_details")
@NoArgsConstructor
@Getter
@Setter
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id = 0;

    private String qty;
    private String subTotal;


    @ManyToOne(cascade = CascadeType.ALL)
    private Product product;

    @ManyToOne
    private Order order;
}
