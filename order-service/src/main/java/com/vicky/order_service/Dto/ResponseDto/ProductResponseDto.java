package com.vicky.order_service.Dto.ResponseDto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {
    private long id;
    private String name;
    private double price;
    private int stockQuantity;
}