package com.vicky.product_service.ServiceImpl;


import com.vicky.product_service.Dto.RequestDto.CategoryRequestDto;
import com.vicky.product_service.Dto.ResponseDto.CategoryResponseDto;
import com.vicky.product_service.Entity.CategoryEntity;
import com.vicky.product_service.Mapper.CategoryMapper;
import com.vicky.product_service.Repository.CategoryRepository;
import com.vicky.product_service.Repository.ProductRepository;
import com.vicky.product_service.Service.CategoryService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<CategoryResponseDto> getCategories() {
        List<CategoryEntity> list = categoryRepository.findAll();
        return list.stream()
                .map(categoryEntity -> CategoryMapper.toDto(categoryEntity))
                .toList();
    }

    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        CategoryEntity categoryEntity = CategoryMapper.toEntity(categoryRequestDto);
        categoryEntity.setCreatedAt(LocalDateTime.now());
        categoryRepository.save(categoryEntity);
        return CategoryMapper.toDto(categoryEntity);
    }

    @Transactional
    @Override
    public void deleteCategory(long id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("category id not found"));
        categoryEntity.setActive(false);
        productRepository.softDeleteAllByCategoryId(id);
        categoryRepository.save(categoryEntity);
    }

    @Override
    public CategoryResponseDto updateCategory(long id, CategoryRequestDto categoryRequestDto) {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("category not found"));
        categoryEntity.setName(categoryRequestDto.getCategoryName());
        categoryRepository.save(categoryEntity);
        return CategoryMapper.toDto(categoryEntity);
    }
}
