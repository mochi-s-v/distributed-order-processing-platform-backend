package com.vicky.payment_gateway_service.ServiceImpl;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.vicky.payment_gateway_service.Service.StripeWebHookService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeWebHookServiceImpl implements StripeWebHookService {

    @Value("${stripe.webhook.key}")
    private String webhookSecret;

    @Override
    public String processEvent(String payload, String sigHeader) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            throw new RuntimeException(e);
        }

        switch (event.getType()) {
            case "checkout.session.completed":
                var session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
                if (session != null) {
                    String orderId = session.getMetadata().get("orderId");
                    System.out.println("received " + event.getType());
                    System.out.println("checkout completed for order: " + orderId);
                }
                break;
            case "payment_intent.payment_failed":
                var failedIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
                if (failedIntent != null) {
                    System.out.println("payment failed-" + failedIntent.getId() + "-" + failedIntent.getAmount());
                    System.out.println("updating db");
                }
                break;
            default:
                System.out.println("unhandled event type : " + event.getType());
        }
        return "ok";
    }
}
