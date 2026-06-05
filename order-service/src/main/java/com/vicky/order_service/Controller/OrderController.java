package com.vicky.order_service.Controller;

import com.vicky.order_service.Dto.ResponseDto.ApiResponse;
import com.vicky.order_service.Dto.ResponseDto.OrderResponseDto;
import com.vicky.order_service.Service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<OrderResponseDto>> checkout(@RequestParam long addressId) {
        OrderResponseDto response = orderService.checkout(addressId);
        return ResponseEntity.ok(ApiResponse.success(response, "Order placed successfully", 201));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> getOrderById(@PathVariable long orderId) {
        OrderResponseDto response = orderService.getOrderById(orderId);
        return ResponseEntity.ok(ApiResponse.success(response, "Order fetched", 200));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponseDto>>> getOrderByUser() {
        List<OrderResponseDto> responseDtoList = orderService.getAllOrdersByUserId();
        return ResponseEntity.ok(ApiResponse.success(responseDtoList, "Orders fetched successfully", 200));
    }
}

