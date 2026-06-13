package com.vicky.payment_gateway_service.Service;

public interface StripeWebHookService {
    public String processEvent(String payload, String sigHeader);
}
