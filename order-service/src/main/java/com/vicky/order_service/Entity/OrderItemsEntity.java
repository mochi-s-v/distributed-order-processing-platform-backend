package com.vicky.order_service.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String productName;
    private int orderCount;
    private double priceAtPurchase;
    private double subTotal;
    @ManyToOne
    @JoinColumn(name = "order_id_fk")
    private OrderEntity orderEntity;
    private long productId; // have to use feign here as well
}
