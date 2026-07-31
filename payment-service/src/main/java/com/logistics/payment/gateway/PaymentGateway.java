package com.logistics.payment.gateway;

import com.logistics.payment.entity.Payment;

public interface PaymentGateway {

	String createGatewayOrder(Payment payment);
}