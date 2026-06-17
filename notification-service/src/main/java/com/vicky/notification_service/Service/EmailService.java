package com.vicky.notification_service.Service;

import com.vicky.notification_service.Dto.KafkaDto.PaymentSuccessEvent;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPaymentMail(PaymentSuccessEvent event) {
        if (event.getEmail() == null || event.getEmail().isBlank()) {
            System.err.println("Cannot send email for Order " + event.getOrderId() + " - Recipient email is NULL!");
            return;
        }
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(event.getEmail());
        mailMessage.setSubject("Payment Confirmed - Order #" + event.getOrderId());
        mailMessage.setText(
                "Hi " + event.getUsername() + ",\n\n" +
                        "Your payment of ₹" + event.getAmount().divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP) + " has been confirmed.\n" +
                        "Order ID: " + event.getOrderId() + "\n\n" +
                        "Thank you for shopping with us!"
        );
        mailSender.send(mailMessage);
    }
}
