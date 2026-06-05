package com.vicky.order_service.Dto.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponseDto {
    private String productName;
    private int quantity;
    private Double priceAtPurchase;
    private Double subTotal;
}