package com.hipradeep.code.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Order entity utilizing the custom sequence generator based on item ID.
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(generator = "order-id-generator")
    @GenericGenerator(
            name = "order-id-generator",
            strategy = "com.hipradeep.code.jpa.OrderIdGenerator",
            parameters = {
                    @Parameter(name = "prefix", value = "ODER_")
            }
    )
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
