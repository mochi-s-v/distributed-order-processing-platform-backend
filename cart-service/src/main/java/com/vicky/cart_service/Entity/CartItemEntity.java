package com.vicky.cart_service.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int quantity;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "cart_id_fk")
    private CartEntity cartEntity;

    @Column(name = "product_id_fk", nullable = false)
    private long productId;
}
