package com.logistics.payment.repository;

import com.logistics.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

	Optional<Payment> findByOrderId(UUID orderId);

	Optional<Payment> findByGatewayOrderId(String gatewayOrderId);

	Optional<Payment> findByGatewayPaymentId(String gatewayPaymentId);
}