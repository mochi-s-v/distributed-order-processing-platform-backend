package com.vicky.payment_gateway_service.Service;

import com.stripe.exception.StripeException;
import com.vicky.payment_gateway_service.Dto.RequestDto.PaymentRequest;

public interface PaymentService {
    public String createCheckoutSession(PaymentRequest request) throws StripeException;
}
