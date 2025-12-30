package com.hipradeep.code.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("products")
public class Product {
    @Id
    private Integer id;

    @NotBlank(message = "Product name is required")
    private String name;

    @Min(value = 1, message = "Quantity should be at least 1")
    private int qty;

    @Positive(message = "Price must be positive")
    private double price;

    private String description;

    @Transient
    private List<Tag> tags;

    public Product(Integer id, String name, int qty, double price) {
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.price = price;
    }
}
