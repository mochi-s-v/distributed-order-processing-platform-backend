package com.vicky.order_service.Mapper;

import com.vicky.order_service.Client.UserClient;
import com.vicky.order_service.Dto.ResponseDto.OrderItemResponseDto;
import com.vicky.order_service.Dto.ResponseDto.OrderResponseDto;
import com.vicky.order_service.Entity.OrderEntity;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class OrderMapper {

    private final UserClient userClient;

    public OrderMapper(UserClient userClient) {
        this.userClient = userClient;
    }

    public OrderResponseDto toDto(OrderEntity entity) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setOrderId(entity.getId());
        dto.setOrderDate(entity.getOrderedAt());
        dto.setStatus(entity.getOrderStatus());
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setAddressResponseDto(userClient.getAddressById(entity.getAddressId()).getData());
        List<OrderItemResponseDto> itemDtos = entity.getOrderItems().stream()
                .map(item -> {
                    OrderItemResponseDto itemDto = new OrderItemResponseDto();
                    itemDto.setProductName(item.getProductName());
                    itemDto.setQuantity(item.getOrderCount());
                    itemDto.setPriceAtPurchase(item.getPriceAtPurchase());
                    itemDto.setSubTotal(item.getOrderCount() * item.getPriceAtPurchase());
                    return itemDto;
                }).toList();
        dto.setOrderItems(itemDtos);
        return dto;
    }
}