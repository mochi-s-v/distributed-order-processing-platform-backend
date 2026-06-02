package com.vicky.product_service.Mapper;


import com.vicky.product_service.Dto.RequestDto.ProductRequestDto;
import com.vicky.product_service.Dto.ResponseDto.ProductResponseDto;
import com.vicky.product_service.Entity.ProductEntity;

public class ProductMapper {
    public static ProductEntity toEntity(ProductRequestDto productRequestDto) {
        if (productRequestDto == null) {
            return null;
        }
        return ProductEntity.builder()
                .name(productRequestDto.getName())
                .description(productRequestDto.getDescription())
                .sku(productRequestDto.getSku())
                .price(productRequestDto.getPrice())
                .quantity(productRequestDto.getQuantity())
                .active(productRequestDto.isActive())
                .build();
    }

    public static ProductResponseDto toDto(ProductEntity productEntity) {
        if (productEntity == null) {
            return null;
        }
        return ProductResponseDto.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .description(productEntity.getDescription())
                .sku(productEntity.getSku())
                .price(productEntity.getPrice())
                .quantity(productEntity.getQuantity())
                .category(productEntity.getCategoryEntity().getName())
                .build();
    }
}
