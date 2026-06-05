package com.vicky.user_service.Controller;

import com.vicky.user_service.Dto.RequestDto.AddressRequestDto;
import com.vicky.user_service.Dto.ResponseDto.AddressResponseDto;
import com.vicky.user_service.Dto.ResponseDto.ApiResponse;
import com.vicky.user_service.Service.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<List<AddressResponseDto>>> addAddress(@RequestBody AddressRequestDto addressRequestDto) {
        List<AddressResponseDto> response = addressService.addAddress(addressRequestDto);
        return ResponseEntity.ok(ApiResponse.success(response, "address added successfully", 200));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse<List<AddressResponseDto>>> deleteAddress(@PathVariable long addressId) {
        List<AddressResponseDto> response = addressService.deleteAddress(addressId);
        return ResponseEntity.ok(ApiResponse.success(response, "address deleted successfully", 200));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressResponseDto>>> getAllAddress() {
        List<AddressResponseDto> response = addressService.getAddress();
        return ResponseEntity.ok(ApiResponse.success(response, "address fetched successfully", 200));
    }

    @GetMapping("/internal/{addressId}")
    public ResponseEntity<ApiResponse<Boolean>> getAddressByIdInternal(@PathVariable long addressId) {
        boolean response = addressService.getAddressByIdValid(addressId);
        return ResponseEntity.ok(ApiResponse.success(response, "address fetched successfully", 200));
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<ApiResponse<AddressResponseDto>> getAddressById(@PathVariable long addressId) {
        AddressResponseDto response = addressService.getAddressById(addressId);
        return ResponseEntity.ok(ApiResponse.success(response, "address fetched successfully", 200));
    }
}
