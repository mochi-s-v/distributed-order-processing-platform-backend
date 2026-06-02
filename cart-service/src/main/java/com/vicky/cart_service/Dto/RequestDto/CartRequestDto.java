package com.vicky.cart_service.Dto.RequestDto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartRequestDto {
    private long productId;
    private int quantity;
}