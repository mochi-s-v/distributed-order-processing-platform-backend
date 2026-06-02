package com.vicky.cart_service.Dto.RequestDto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDetailDto {
    private long productId;
    private String productName;
    private Double price;
    private int quantity;
    private Double subTotal;
}