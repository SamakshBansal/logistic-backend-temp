package com.logistics.order.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.logistics.order.entity.Order;
import com.logistics.order.enums.OrderStatus;
import com.logistics.order.enums.PaymentStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

	List<Order> findByCustomerId(UUID customerId);

	List<Order> findByAssignedAgentId(UUID assignedAgentId);

	Optional<Order> findByTrackingNumber(String trackingNumber);

	boolean existsByTrackingNumber(String trackingNumber);

	long countByPaymentStatus(PaymentStatus paymentStatus);

	long countByOrderStatus(OrderStatus orderStatus);
}