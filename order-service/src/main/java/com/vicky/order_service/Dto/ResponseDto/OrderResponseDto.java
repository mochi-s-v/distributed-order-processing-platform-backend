package com.vicky.order_service.Dto.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {
    private long orderId;
    private LocalDateTime orderDate;
    private String status;
    private Double totalAmount;
    private AddressResponseDto addressResponseDto;
    private List<OrderItemResponseDto> orderItems;
}