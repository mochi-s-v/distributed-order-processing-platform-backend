package com.vicky.order_service.Consumer;

import com.vicky.order_service.Dto.KafkaDto.PaymentSuccessEvent;
import com.vicky.order_service.Service.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentConsumer {

    private final OrderService orderService;

    public PaymentConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "payment-events", groupId = "order-service-group")
    public void handlePaymentSuccess(PaymentSuccessEvent event) {
        // change payment status
        orderService.changeOrderStatus(event.getOrderId());
        // reduce stock
        orderService.reduceStock(event.getOrderId());
    }
}
