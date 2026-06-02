package com.vicky.cart_service.Mapper;



import com.vicky.cart_service.Client.ProductClient;
import com.vicky.cart_service.Dto.RequestDto.CartItemDetailDto;
import com.vicky.cart_service.Dto.ResponseDto.ApiResponse;
import com.vicky.cart_service.Dto.ResponseDto.CartResponseDto;
import com.vicky.cart_service.Dto.ResponseDto.ProductResponseDto;
import com.vicky.cart_service.Entity.CartEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartMapper {

    private final ProductClient productClient;

    public CartMapper(ProductClient productClient) {
        this.productClient = productClient;
    }

    public CartResponseDto toDto(CartEntity cartEntity) {
        CartResponseDto response = new CartResponseDto();
        response.setCartId(cartEntity.getId());


        List<CartItemDetailDto> itemDetails = cartEntity.getCartItems().stream()
                .map(item -> {
                    CartItemDetailDto detail = new CartItemDetailDto();
                    ApiResponse<ProductResponseDto> productResponse = productClient.getProduct(item.getProductId());
                    if (productResponse != null && productResponse.getData() != null) {
                        ProductResponseDto product = productResponse.getData();
                        detail.setProductId(product.getId());
                        detail.setProductName(product.getName());
                        detail.setPrice(product.getPrice());
                        detail.setQuantity(item.getQuantity());
                        detail.setSubTotal(item.getQuantity() * product.getPrice());
                    } else {
                        detail.setProductName("Unknown Product (" + item.getProductId() + ")");
                        detail.setPrice(0.0);
                        detail.setSubTotal(0.0);
                    }
                    return detail;
                }).toList();
        response.setItems(itemDetails);
        double total = itemDetails.stream()
                .mapToDouble(item -> item.getSubTotal())
                .sum();
        response.setTotalCartPrice(total);
        return response;
    }
}