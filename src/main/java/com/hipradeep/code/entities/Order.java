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
public class Order {

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

    public Order() { }

    public Order(String product, int quantity, double price) {
        this.productName= product;
        this.quantity = quantity;
        this.price = price;
    }

    @PrePersist
    public void prePersist() {
        System.out.println("[LIFECYCLE] PrePersist - Order about to be created: " + this);
        if (this.quantity == null) {
            this.quantity = 1; // Default quantity
        }
    }

    @PostPersist
    public void postPersist() {
        System.out.println("[LIFECYCLE] PostPersist - Order created with ID: " + this.id);
    }

    @PostLoad
    public void postLoad() {
        System.out.println("[LIFECYCLE] PostLoad - Order loaded from DB: " + this.id);
    }

    @PreUpdate
    public void preUpdate() {
        System.out.println("[LIFECYCLE] PreUpdate - Order about to be updated: " + this.id);
        this.updatedAt = LocalDateTime.now();
    }

    @PostUpdate
    public void postUpdate() {
        System.out.println("[LIFECYCLE] PostUpdate - Order updated: " + this.id);
    }

    @PreRemove
    public void preRemove() {
        System.out.println("[LIFECYCLE] PreRemove - Order about to be deleted: " + this.id);
    }

    @PostRemove
    public void postRemove() {
        System.out.println("[LIFECYCLE] PostRemove - Order deleted: " + this.id);
    }
}
