package com.vicky.order_service.Client;

import com.vicky.order_service.Config.FeignClientConfig;
import com.vicky.order_service.Dto.ResponseDto.AddressResponseDto;
import com.vicky.order_service.Dto.ResponseDto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "USER-SERVICE",
        path = "/api/address",
        configuration = FeignClientConfig.class)
public interface UserClient {

    @GetMapping("/internal/{addressId}")
    ApiResponse<Boolean> getAddressByIdInternal(@PathVariable long addressId);

    @GetMapping("/{addressId}")
    ApiResponse<AddressResponseDto> getAddressById(@PathVariable long addressId);
}
