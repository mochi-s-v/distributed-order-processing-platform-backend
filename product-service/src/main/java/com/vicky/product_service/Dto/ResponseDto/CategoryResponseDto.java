package com.vicky.product_service.Dto.ResponseDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDto {
    private long id;
    private String categoryName;
    private boolean active;
}
