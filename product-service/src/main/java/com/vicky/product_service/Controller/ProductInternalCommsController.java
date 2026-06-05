package com.vicky.product_service.Controller;

import com.vicky.product_service.Dto.RequestDto.StockDeductRequestDto;
import com.vicky.product_service.Dto.ResponseDto.ApiResponse;
import com.vicky.product_service.Service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/internal/product")
public class ProductInternalCommsController {

    private final ProductService productService;

    public ProductInternalCommsController(ProductService productService) {
        this.productService = productService;
    }

    @PutMapping("/deduct-stock")
    public ResponseEntity<ApiResponse<Void>> deductStock(@RequestBody List<StockDeductRequestDto> stockDeductRequestDtoList) {
        productService.deduceQuantity(stockDeductRequestDtoList);
        return ResponseEntity.ok(
                ApiResponse.success(
                        null,
                        "Quantity deduced successfully",
                        200));
    }
}
