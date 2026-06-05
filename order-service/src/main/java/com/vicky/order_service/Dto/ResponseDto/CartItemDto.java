package com.vicky.order_service.Dto.ResponseDto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    private long productId;
    private String productName;
    private int quantity;
    private double price;
    private double subTotal;
}