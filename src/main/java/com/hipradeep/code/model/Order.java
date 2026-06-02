package com.hipradeep.code.model;

import com.hipradeep.code.jpa.OrderIdGeneration;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Order entity utilizing the custom sequence generator based on item ID.
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @OrderIdGeneration(prefix = "ODER_")
    private String id;

    private Long itemId;

    private String customerName;

    private int quantity;

    public Order() {
    }

    public Order(Long itemId, String customerName, int quantity) {
        this.itemId = itemId;
        this.customerName = customerName;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
