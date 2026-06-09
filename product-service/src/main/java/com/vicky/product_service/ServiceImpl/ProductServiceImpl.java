package com.vicky.product_service.ServiceImpl;


import com.vicky.product_service.Dto.RequestDto.ProductRequestDto;
import com.vicky.product_service.Dto.RequestDto.StockDeductRequestDto;
import com.vicky.product_service.Dto.ResponseDto.ProductResponseDto;
import com.vicky.product_service.Entity.CategoryEntity;
import com.vicky.product_service.Entity.ProductEntity;
import com.vicky.product_service.Mapper.ProductMapper;
import com.vicky.product_service.Repository.CategoryRepository;
import com.vicky.product_service.Repository.ProductRepository;
import com.vicky.product_service.Service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<ProductResponseDto> getAllProduct(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductEntity> pageDetails = productRepository.findAll(pageable);
        return pageDetails.getContent().stream()
                .map(productEntity -> ProductMapper.toDto(productEntity))
                .toList();
    }

    @Override
    public ProductResponseDto getProduct(long id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("product not found"));
        return ProductMapper.toDto(productEntity);
    }

    @Override
    public List<ProductResponseDto> getProductByCategory(long id) {
        List<ProductEntity> list = productRepository.getByCategoryEntity_Id(id);
        return list.stream()
                .map(productEntity -> ProductMapper.toDto(productEntity))
                .toList();
    }

    @Override
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        CategoryEntity categoryEntity = categoryRepository.findById(productRequestDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("category not found"));
        ProductEntity productEntity = ProductMapper.toEntity(productRequestDto);
        productEntity.setCreatedAt(LocalDateTime.now());
        productEntity.setUpdatedAt(LocalDateTime.now());
        productEntity.setCategoryEntity(categoryEntity);
        productRepository.save(productEntity);
        return ProductMapper.toDto(productEntity);
    }

    @Override
    @Transactional
    public ProductResponseDto updateProduct(long id, ProductRequestDto productRequestDto) {
        CategoryEntity categoryEntity = categoryRepository.findById(productRequestDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("category not found"));
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("product not found"));
        productEntity.setName(productRequestDto.getName());
        productEntity.setDescription(productRequestDto.getDescription());
        productEntity.setSku(productRequestDto.getSku());
        productEntity.setPrice(productRequestDto.getPrice());
        productEntity.setQuantity(productRequestDto.getQuantity());
        productEntity.setUpdatedAt(LocalDateTime.now());
        productEntity.setCategoryEntity(categoryEntity);
        productRepository.save(productEntity);
        return ProductMapper.toDto(productEntity);
    }

    @Override
    public void deleteProduct(long id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("no product found"));
        productEntity.setActive(false);
        productRepository.save(productEntity);
    }

    @Override
    @Transactional
    public void deduceQuantity(List<StockDeductRequestDto> stockDeductRequestDtoList) {
        stockDeductRequestDtoList.forEach(
                request -> {
                    ProductEntity productEntity = productRepository.findById(request.getProductId())
                            .orElseThrow(() -> new RuntimeException("no product found"));

                    if (productEntity.getQuantity() < request.getQuantity()) {
                        throw new RuntimeException("Insufficient stock for product: " + productEntity.getName());
                    }
                    System.out.println("im in deduce quantity service impl");

                    productEntity.setQuantity(productEntity.getQuantity() - request.getQuantity());
                    productRepository.save(productEntity);
                });
    }
}
