package com.vicky.payment_gateway_service.Repository;

import com.vicky.payment_gateway_service.Entity.PaymentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentRecord, Long>{
    Optional<PaymentRecord> findByStripeSessionId(String stripeSessionId);
}
