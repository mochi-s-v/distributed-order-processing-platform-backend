package com.vicky.product_service.Service;


import com.vicky.product_service.Dto.RequestDto.ProductRequestDto;
import com.vicky.product_service.Dto.ResponseDto.ProductResponseDto;

import java.util.List;

public interface ProductService {

    // users
    public List<ProductResponseDto> getAllProduct(int page, int size);
    public ProductResponseDto getProduct(long id);
    public List<ProductResponseDto> getProductByCategory(long id);

    // admins
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto);
    public ProductResponseDto updateProduct(long id, ProductRequestDto productRequestDto);
    public void deleteProduct(long id);
}
