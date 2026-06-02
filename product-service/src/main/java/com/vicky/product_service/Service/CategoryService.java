package com.vicky.product_service.Service;


import com.vicky.product_service.Dto.RequestDto.CategoryRequestDto;
import com.vicky.product_service.Dto.ResponseDto.CategoryResponseDto;

import java.util.List;

public interface CategoryService {

    // users
    public List<CategoryResponseDto> getCategories();

    // admins
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto);
    public void deleteCategory(long id);
    public CategoryResponseDto updateCategory(long id, CategoryRequestDto categoryRequestDto);
}
