package com.vicky.order_service.Consumer;

import com.vicky.order_service.Dto.KafkaDto.PaymentSuccessEvent;
import com.vicky.order_service.Service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentConsumer {

    private final OrderService orderService;

    public PaymentConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "payment-events", groupId = "order-service-group")
    public void handlePaymentSuccess(PaymentSuccessEvent event) {
        // change payment status
        System.out.println("request inside the payment consumer in order service");
        System.out.println("changing order status");
        System.out.println("reducing stock");
        orderService.changeOrderStatus(event.getOrderId());
        // reduce stock
        orderService.reduceStock(event.getOrderId());
        //clear cart
        System.out.println("Clearing cart items");
        try {
            orderService.clearCart(event.getUsername());
        } catch (Exception e) {
            log.warn("Cart clear failed for order {}, skipping: {}", event.getOrderId(), e.getMessage());
        }
    }
}
