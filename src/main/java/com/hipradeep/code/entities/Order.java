package com.hipradeep.code.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id = 0;

    @Column(name = "customer_id", nullable = false)
    private String customer;

    private String total;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private Set<OrderDetails> orderDetails;
}
