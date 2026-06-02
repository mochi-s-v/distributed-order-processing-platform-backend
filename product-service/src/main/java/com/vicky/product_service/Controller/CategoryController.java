package com.vicky.product_service.Controller;


import com.vicky.product_service.Dto.RequestDto.CategoryRequestDto;
import com.vicky.product_service.Dto.ResponseDto.ApiResponse;
import com.vicky.product_service.Dto.ResponseDto.CategoryResponseDto;
import com.vicky.product_service.Service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> createCategory(@RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryResponseDto categoryResponseDto = categoryService.createCategory(categoryRequestDto);
        return ResponseEntity.ok(ApiResponse.success(categoryResponseDto, "Category created successfully", 200));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> getCategories() {
        return ResponseEntity.ok(ApiResponse.success(categoryService.getCategories(), "fetched categories successfully", 200));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> updateCategory(@PathVariable long categoryId, @RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryResponseDto categoryResponseDto = categoryService.updateCategory(categoryId, categoryRequestDto);
        return ResponseEntity.ok(ApiResponse.success(categoryResponseDto, "Category updated successfully", 200));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(null, "category deleted successfully", 200));
    }
}
