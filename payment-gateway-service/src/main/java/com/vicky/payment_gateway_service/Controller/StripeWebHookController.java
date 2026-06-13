package com.vicky.payment_gateway_service.Controller;

import com.vicky.payment_gateway_service.Service.StripeWebHookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class StripeWebHookController {

    private final StripeWebHookService stripeWebhookService;

    public StripeWebHookController(StripeWebHookService stripeWebhookService) {
        this.stripeWebhookService = stripeWebhookService;
    }

    @PostMapping("/stripe")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        System.out.println("Im in webhook/stripe controller");
        return ResponseEntity.ok(stripeWebhookService.processEvent(payload, sigHeader));
    }
}