package com.hipradeep.code.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "sb_orders")
@EntityListeners(AuditingEntityListener.class)
public class Order2 {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String productName;
    private Integer quantity;
    private Double price;

    @CreatedBy
    private String createdBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Version
    private Integer version;

    public Order2() {
        // Default constructor
    }

    public Order2(String product, int quantity, double price) {
        this.productName = product;
        this.quantity = quantity;
        this.price = price;
    }
}
