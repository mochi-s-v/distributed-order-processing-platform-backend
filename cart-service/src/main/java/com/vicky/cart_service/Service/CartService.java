package com.vicky.cart_service.Service;


import com.vicky.cart_service.Dto.RequestDto.CartRequestDto;
import com.vicky.cart_service.Dto.ResponseDto.CartResponseDto;

public interface CartService {
    CartResponseDto addItemToCart(CartRequestDto cartRequestDto);
    CartResponseDto updateItemQuantity(long productId, int newQuantity);
    CartResponseDto getCart();
    CartResponseDto removeItemFromCart(long productId);
    void clearCart();
}
