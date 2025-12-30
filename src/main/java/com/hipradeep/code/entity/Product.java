package com.hipradeep.code.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("products")
public class Product {
    @Id
    private Integer id;
    private String name;
    private int qty;
    private double price;

    @Transient
    private List<Tag> tags;

    public Product(Integer id, String name, int qty, double price) {
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.price = price;
    }
}
