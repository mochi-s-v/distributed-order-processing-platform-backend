package com.vicky.order_service.Dto.ResponseDto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CartResponseDto {
    private String username;
    private double totalCartPrice;
    private List<CartItemDto> items;
}