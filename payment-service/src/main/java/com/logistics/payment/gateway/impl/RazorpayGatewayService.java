package com.logistics.payment.gateway.impl;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.logistics.payment.config.RazorpayProperties;
import com.logistics.payment.entity.Payment;
import com.logistics.payment.exception.PaymentGatewayException;
import com.logistics.payment.gateway.PaymentGateway;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RazorpayGatewayService implements PaymentGateway {

	private final RazorpayClient razorpayClient;
	private final RazorpayProperties razorpayProperties;

	@Override
	public String createGatewayOrder(Payment payment) {

		try {

			JSONObject options = new JSONObject();

			// Razorpay expects amount in paise
			long amount = payment.getAmount().multiply(BigDecimal.valueOf(100)).longValue();

			options.put("amount", amount);
			options.put("currency", razorpayProperties.getCurrency());

			// My internal payment ID
			options.put("receipt", payment.getId().toString());

			Order order = razorpayClient.orders.create(options);

			return order.get("id").toString();

		} catch (RazorpayException e) {
			throw new PaymentGatewayException("Failed to create Razorpay Order", e);
		}

	}

}