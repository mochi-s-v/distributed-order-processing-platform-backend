package com.vicky.cart_service.Controller;


import com.vicky.cart_service.Dto.RequestDto.CartRequestDto;
import com.vicky.cart_service.Dto.ResponseDto.ApiResponse;
import com.vicky.cart_service.Dto.ResponseDto.CartResponseDto;
import com.vicky.cart_service.Service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartResponseDto>> addItem(@RequestBody CartRequestDto dto) {
        CartResponseDto result = cartService.addItemToCart(dto);
        return ResponseEntity.ok(ApiResponse.success(result, "Item added to cart", 200));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponseDto>> getCart() {
        CartResponseDto result = cartService.getCart();
        return ResponseEntity.ok(ApiResponse.success(result, "Cart fetched successfully", 200));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<Void>> clearCart() {
        cartService.clearCart();
        return ResponseEntity.ok(ApiResponse.success(null, "Cart cleared successfully", 200));
    }

    @DeleteMapping("/item/{productId}")
    public ResponseEntity<ApiResponse<CartResponseDto>> removeItem(@PathVariable long productId) {
        CartResponseDto updatedCart = cartService.removeItemFromCart(productId);
        return ResponseEntity.ok(ApiResponse.success(updatedCart, "Item removed from cart", 200));
    }

    @PutMapping("/update/{productId}/{quantity}")
    public ResponseEntity<ApiResponse<CartResponseDto>> updateQuantity(
            @PathVariable long productId,
            @PathVariable int quantity) {
        CartResponseDto result = cartService.updateItemQuantity(productId, quantity);
        return ResponseEntity.ok(ApiResponse.success(result, "Quantity updated", 200));
    }
}