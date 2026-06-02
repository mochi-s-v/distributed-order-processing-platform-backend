package com.vicky.product_service.Dto.ResponseDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDto {
    private long id;
    private String name;
    private String description;
    private String sku;
    private Double price;
    private int quantity;
    private String category;
}
