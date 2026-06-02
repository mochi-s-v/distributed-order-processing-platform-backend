package com.vicky.cart_service.Dto.ResponseDto;

import com.vicky.cart_service.Dto.RequestDto.CartItemDetailDto;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDto {
    private long cartId;
    private List<CartItemDetailDto> items;
    private Double totalCartPrice;
}