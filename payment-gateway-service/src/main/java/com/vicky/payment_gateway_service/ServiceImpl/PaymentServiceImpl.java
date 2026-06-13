package com.vicky.payment_gateway_service.ServiceImpl;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.vicky.payment_gateway_service.Dto.RequestDto.PaymentRequest;
import com.vicky.payment_gateway_service.Entity.PaymentRecord;
import com.vicky.payment_gateway_service.Service.PaymentService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${stripe.client.url}")
    private String clientUrl;


    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    @Override
    public String createCheckoutSession(PaymentRequest request) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(clientUrl + "/payment/success?orderId=" + request.getOrderId())
                .setCancelUrl(clientUrl + "/payment/cancel?orderId=" + request.getOrderId())
                .setCustomerEmail(request.getCustomerEmail())
                .putMetadata("orderId", request.getOrderId())
                .addLineItem(SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(request.getCurrency())
                                                .setUnitAmount(request.getAmount())
                                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Order ID: " + request.getOrderId()
                                                                ).build()
                                                ).build()
                                ).build()
                ).build();
        Session session = Session.create(params);
        return session.getUrl();
    }
}
