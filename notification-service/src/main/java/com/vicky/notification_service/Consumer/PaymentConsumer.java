package com.vicky.notification_service.Consumer;

import com.vicky.notification_service.Dto.KafkaDto.PaymentSuccessEvent;
import com.vicky.notification_service.Service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentConsumer {

    private final EmailService emailService;

    public PaymentConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "payment-events", groupId = "notification-service-group")
    public void handlePaymentSuccess(PaymentSuccessEvent event) {
        System.out.println("received payment event for order: " + event.getOrderId());
        emailService.sendPaymentMail(event);
        System.out.println("email sent to: " + event.getEmail());
    }
}
