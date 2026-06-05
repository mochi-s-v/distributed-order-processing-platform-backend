package com.vicky.order_service.Service;


import com.vicky.order_service.Dto.ResponseDto.OrderResponseDto;

import java.util.List;

public interface OrderService {
    public OrderResponseDto checkout(long addressId);
    OrderResponseDto getOrderById(long orderId);
    List<OrderResponseDto> getAllOrdersByUserId();
}
