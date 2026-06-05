package com.vicky.order_service.Dto.ResponseDto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {
        private LocalDateTime createdAt;
        private int statusCode;
        private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data, String message, int statusCode) {
        return ApiResponse.<T>builder()
                .message(message)
                .createdAt(LocalDateTime.now())
                .statusCode(statusCode)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(String message, int statusCode) {
        return ApiResponse.<T>builder()
                .message(message)
                .createdAt(LocalDateTime.now())
                .statusCode(statusCode)
                .data(null)
                .build();
    }
}
