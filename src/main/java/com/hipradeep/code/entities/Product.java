package com.hipradeep.code.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "products")
@NoArgsConstructor
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id = 0;

    @Column(name = "product_name", nullable = false)
    private String pName;

    private String price;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private Set<OrderDetails> orderDetails;
}
