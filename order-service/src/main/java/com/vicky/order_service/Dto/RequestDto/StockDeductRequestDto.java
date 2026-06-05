package com.vicky.order_service.Dto.RequestDto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockDeductRequestDto {
    private long productId;
    private int quantity;
}