package com.vicky.cart_service.Client;

import com.vicky.cart_service.Config.FeignClientConfig;
import com.vicky.cart_service.Dto.ResponseDto.ApiResponse;
import com.vicky.cart_service.Dto.ResponseDto.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PRODUCT-SERVICE",
        path = "/api/product",
        configuration = FeignClientConfig.class)
public interface ProductClient {

    @GetMapping("/{productId}")
    ApiResponse<ProductResponseDto> getProduct(@PathVariable("productId") long productId);
}
