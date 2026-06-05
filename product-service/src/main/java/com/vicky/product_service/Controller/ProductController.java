package com.vicky.product_service.Controller;


import com.vicky.product_service.Dto.RequestDto.ProductRequestDto;
import com.vicky.product_service.Dto.ResponseDto.ApiResponse;
import com.vicky.product_service.Dto.ResponseDto.ProductResponseDto;
import com.vicky.product_service.Service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<ProductResponseDto>> createProduct(@RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto productResponseDto = productService.createProduct(productRequestDto);
        return ResponseEntity.ok(ApiResponse.success(productResponseDto, "product created successfully", 200));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> updateProduct(@PathVariable long productId, @RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto productResponseDto = productService.updateProduct(productId, productRequestDto);
        return ResponseEntity.ok(ApiResponse.success(productResponseDto, "product updated successfully", 200));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(ApiResponse.success(null, "product deleted successfully", 200));
    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                                                                @RequestParam(defaultValue = "10") int size) {
        List<ProductResponseDto> list = productService.getAllProduct(page, size);
        return ResponseEntity.ok(ApiResponse.success(list, "all products fetched successfully", 200));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProduct(@PathVariable long productId) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        productService.getProduct(productId),
                        "product fetched successfully",
                        200));
    }

    @GetMapping("/categoryId/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getProductByCategory(@PathVariable long categoryId) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        productService.getProductByCategory(categoryId),
                        "product fetched based on category",
                        200));
    }
}
