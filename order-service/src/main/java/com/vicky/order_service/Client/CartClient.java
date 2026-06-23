package com.vicky.order_service.Client;

import com.vicky.order_service.Config.FeignClientConfig;
import com.vicky.order_service.Dto.ResponseDto.ApiResponse;
import com.vicky.order_service.Dto.ResponseDto.CartResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "CART-SERVICE",
        path = "/api/cart",
        configuration = FeignClientConfig.class)
public interface CartClient {

    @GetMapping
    ApiResponse<CartResponseDto> getCart();

    @DeleteMapping("/internal/clear")
    ApiResponse<Void> clearCartInternal(@RequestParam("username") String username);
}
