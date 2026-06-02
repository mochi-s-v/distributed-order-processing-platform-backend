package com.vicky.product_service.Mapper;


import com.vicky.product_service.Dto.RequestDto.CategoryRequestDto;
import com.vicky.product_service.Dto.ResponseDto.CategoryResponseDto;
import com.vicky.product_service.Entity.CategoryEntity;

public class CategoryMapper {
    public static CategoryEntity toEntity(CategoryRequestDto categoryRequestDto) {
        if (categoryRequestDto == null) {
            return null;
        }
        return CategoryEntity.builder()
                .name(categoryRequestDto.getCategoryName())
                .active(categoryRequestDto.isActive())
                .build();
    }

    public static CategoryResponseDto toDto(CategoryEntity categoryEntity) {
        if (categoryEntity == null) {
            return null;
        }

        return CategoryResponseDto.builder()
                .id(categoryEntity.getId())
                .categoryName(categoryEntity.getName())
                .active(categoryEntity.isActive())
                .build();
    }
}
