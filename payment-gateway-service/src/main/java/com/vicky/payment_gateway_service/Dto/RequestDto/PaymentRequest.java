package com.vicky.payment_gateway_service.Dto.RequestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private String orderId;
    private Long amount;
    private String currency;
    private String customerEmail;
    private String username;
}
