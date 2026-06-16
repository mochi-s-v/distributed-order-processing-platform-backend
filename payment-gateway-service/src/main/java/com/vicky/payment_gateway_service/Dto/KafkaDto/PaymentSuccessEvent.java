package com.vicky.payment_gateway_service.Dto.KafkaDto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentSuccessEvent {
    private Long orderId;
    private String username;
    private BigDecimal amount;
    private String stripeSessionId;
}
