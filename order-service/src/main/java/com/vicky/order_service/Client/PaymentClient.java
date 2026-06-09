package com.vicky.order_service.Client;


import com.vicky.order_service.Dto.RequestDto.PaymentRequest;
import com.vicky.order_service.Dto.ResponseDto.ApiResponse;
import com.vicky.order_service.Dto.ResponseDto.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-gateway-service")
public interface PaymentClient {

    @PostMapping("/api/internal/payment/checkout")
    ApiResponse<PaymentResponse> initiateCheckout(@RequestBody PaymentRequest request);
}