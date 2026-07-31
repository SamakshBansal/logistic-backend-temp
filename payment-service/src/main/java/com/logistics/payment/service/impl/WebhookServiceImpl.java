package com.logistics.payment.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.logistics.payment.client.OrderClient;
import com.logistics.payment.dto.request.UpdatePaymentStatusRequest;
import com.logistics.payment.entity.Payment;
import com.logistics.payment.enums.PaymentStatus;
import com.logistics.payment.exception.PaymentNotFoundException;
import com.logistics.payment.exception.PaymentVerificationException;
import com.logistics.payment.repository.PaymentRepository;
import com.logistics.payment.service.WebhookService;
import com.logistics.payment.util.RazorpayWebhookUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WebhookServiceImpl implements WebhookService {

	private final ObjectMapper objectMapper;

	private final PaymentRepository paymentRepository;

	private final OrderClient orderClient;

	private final RazorpayWebhookUtil webhookUtil;

	@Override
	@Transactional
	public void processWebhook(String payload, String signature) {

		try {

			// Verify Razorpay webhook signature
			if (!webhookUtil.verify(payload, signature)) {
				throw new PaymentVerificationException("Invalid Razorpay Webhook Signature");
			}

			// Parse JSON
			JsonNode root = objectMapper.readTree(payload);

			// Get event
			String event = root.path("event").asText();

			// Ignore all events except payment.captured
			if (!"payment.captured".equals(event)) {
				return;
			}

			// Extract payment entity
			JsonNode entity = root.path("payload").path("payment").path("entity");

			String gatewayPaymentId = entity.path("id").asText();

			String gatewayOrderId = entity.path("order_id").asText();

			// Find payment
			Payment payment = paymentRepository.findByGatewayOrderId(gatewayOrderId)
					.orElseThrow(() -> new PaymentNotFoundException("Payment not found"));

			// Prevent duplicate processing
			if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
				return;
			}

			// Update payment
			payment.setGatewayPaymentId(gatewayPaymentId);
			payment.setPaymentStatus(PaymentStatus.SUCCESS);

			paymentRepository.save(payment);

			// Notify Order Service
			UpdatePaymentStatusRequest request = new UpdatePaymentStatusRequest();
			request.setPaymentStatus(PaymentStatus.SUCCESS);

			orderClient.updatePaymentStatus(payment.getOrderId(), request);

		} catch (PaymentVerificationException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new RuntimeException("Failed to process Razorpay webhook", ex);
		}
	}
}