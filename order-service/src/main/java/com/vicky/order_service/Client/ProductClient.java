package com.vicky.order_service.Client;

import com.vicky.order_service.Config.FeignClientConfig;
import com.vicky.order_service.Dto.RequestDto.StockDeductRequestDto;
import com.vicky.order_service.Dto.ResponseDto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@FeignClient(name = "PRODUCT-SERVICE",
        path = "/api/internal/product",
        configuration = FeignClientConfig.class)
public interface ProductClient {

    @PutMapping("/deduct-stock")
    ApiResponse<Void> deductStock(@RequestBody List<StockDeductRequestDto> stockDeductRequestDto);

}
