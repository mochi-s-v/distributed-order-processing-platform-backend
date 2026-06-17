package com.vicky.payment_gateway_service.ServiceImpl;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.vicky.payment_gateway_service.Dto.KafkaDto.PaymentSuccessEvent;
import com.vicky.payment_gateway_service.Service.StripeWebHookService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StripeWebHookServiceImpl implements StripeWebHookService {

    @Value("${stripe.webhook.key}")
    private String webhookSecret;

    private final KafkaTemplate<String, PaymentSuccessEvent> kafkaTemplate;

    public StripeWebHookServiceImpl(KafkaTemplate<String, PaymentSuccessEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

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
                    String username = session.getMetadata().get("username");
                    String customerEmail1 = session.getCustomerEmail();
                    String customerEmail2 = session.getMetadata().get("email");
                    System.out.println("received " + event.getType());
                    System.out.println("checkout completed for order: " + orderId);
                    System.out.println("after webhook customer email from session : " + customerEmail1);
                    System.out.println("after webhook customer email from metadata : " + customerEmail2);
                    kafkaTemplate.send("payment-events",
                            orderId,
                            new PaymentSuccessEvent(Long.parseLong(orderId),
                                    username,
                                    BigDecimal.valueOf(session.getAmountTotal()),
                                    session.getId(), customerEmail2));
                    System.out.println("published to kafka for order: " + orderId);
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
