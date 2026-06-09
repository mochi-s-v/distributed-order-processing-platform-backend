package com.vicky.payment_gateway_service.Controller;

import com.stripe.exception.StripeException;
import com.vicky.payment_gateway_service.Dto.RequestDto.PaymentRequest;
import com.vicky.payment_gateway_service.Dto.ResponseDto.ApiResponse;
import com.vicky.payment_gateway_service.Dto.ResponseDto.PaymentResponse;
import com.vicky.payment_gateway_service.ServiceImpl.PaymentServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/payment")
public class PaymentInternalController {

    private final PaymentServiceImpl paymentService;

    public PaymentInternalController(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<PaymentResponse>> checkout(@RequestBody PaymentRequest request) {
        try {
            String stripeUrl = paymentService.createCheckoutSession(request);
            return ResponseEntity.ok(ApiResponse.success(new PaymentResponse(stripeUrl),
                    "payment url fetched",
                    200));
        } catch (StripeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
