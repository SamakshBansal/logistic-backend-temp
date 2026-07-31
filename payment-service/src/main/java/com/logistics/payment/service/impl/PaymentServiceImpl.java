package com.logistics.payment.service.impl;

import com.logistics.payment.client.OrderClient;
import com.logistics.payment.config.RazorpayProperties;
import com.logistics.payment.dto.event.NotificationEvent;
import com.logistics.payment.dto.request.CreatePaymentRequest;
import com.logistics.payment.dto.request.UpdatePaymentStatusRequest;
import com.logistics.payment.dto.request.VerifyPaymentRequest;
import com.logistics.payment.dto.response.OrderPaymentDetailsResponse;
import com.logistics.payment.dto.response.PaymentResponse;
import com.logistics.payment.entity.Payment;
import com.logistics.payment.enums.NotificationEventType;
import com.logistics.payment.enums.PaymentStatus;
import com.logistics.payment.exception.PaymentAlreadyExistsException;
import com.logistics.payment.exception.PaymentNotFoundException;
//import com.logistics.payment.exception.PaymentVerificationException;
import com.logistics.payment.gateway.PaymentGateway;
import com.logistics.payment.repository.PaymentRepository;
import com.logistics.payment.service.NotificationPublisher;
import com.logistics.payment.service.PaymentService;
//import com.logistics.payment.util.RazorpaySignatureUtil;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepository;
	private final OrderClient orderClient;
	private final PaymentGateway paymentGateway;
//	private final RazorpaySignatureUtil razorpaySignatureUtil;
	private final RazorpayProperties razorpayProperties;
	private final NotificationPublisher notificationPublisher;

	@Override
	public PaymentResponse createPayment(CreatePaymentRequest request) {

		Optional<Payment> existingPayment = paymentRepository.findByOrderId(request.getOrderId());

		if (existingPayment.isPresent()) {

			Payment payment = existingPayment.get();

			if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
				throw new PaymentAlreadyExistsException("Payment has already been completed.");
			}

			return mapToResponse(payment);
		}

		OrderPaymentDetailsResponse order = orderClient.getPaymentDetails(request.getOrderId());

		if (order.getPaymentStatus() == PaymentStatus.SUCCESS) {
			throw new PaymentAlreadyExistsException("Order is already paid");
		}

		Payment payment = Payment.builder().orderId(request.getOrderId()).customerId(order.getCustomerId())
				.amount(order.getAmount()).currency("INR").paymentStatus(PaymentStatus.PENDING)
				.paymentMethod("RAZORPAY").paymentLink(null).build();

		Payment savedPayment = paymentRepository.save(payment);

		String gatewayOrderId = paymentGateway.createGatewayOrder(savedPayment);

		savedPayment.setGatewayOrderId(gatewayOrderId);

		savedPayment = paymentRepository.save(savedPayment);

		return mapToResponse(savedPayment);
	}

	private PaymentResponse mapToResponse(Payment payment) {

		return PaymentResponse.builder()

				.paymentId(payment.getId()).orderId(payment.getOrderId()).customerId(payment.getCustomerId())
				.amount(payment.getAmount()).currency(payment.getCurrency()).gatewayOrderId(payment.getGatewayOrderId())
				.paymentStatus(payment.getPaymentStatus()).gatewayPaymentId(payment.getGatewayPaymentId())
				.paymentMethod(payment.getPaymentMethod()).paymentLink(payment.getPaymentLink())
				.razorpayKey(razorpayProperties.getKeyId()).createdAt(payment.getCreatedAt())

				.build();

	}

	@Override
	@Transactional
	public PaymentResponse verifyPayment(VerifyPaymentRequest request) {

		Payment payment = paymentRepository.findByGatewayOrderId(request.getGatewayOrderId())
				.orElseThrow(() -> new PaymentNotFoundException("Payment not found"));

		if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
			throw new PaymentAlreadyExistsException("Payment already verified");
		}

//		boolean valid = razorpaySignatureUtil.verifySignature(request.getGatewayOrderId(),
//				request.getGatewayPaymentId(), request.getSignature());
//
//		if (!valid) {
//			throw new PaymentVerificationException("Invalid Razorpay Signature");
//		}

		payment.setGatewayPaymentId(request.getGatewayPaymentId());

		payment.setPaymentStatus(PaymentStatus.SUCCESS);

		payment.setPaymentMethod("RAZORPAY");

		paymentRepository.save(payment);

		UpdatePaymentStatusRequest updateRequest = new UpdatePaymentStatusRequest();

		updateRequest.setPaymentStatus(PaymentStatus.SUCCESS);

		try {
			System.out.println("Calling Order Service...");
			orderClient.updatePaymentStatus(payment.getOrderId(), updateRequest);

			NotificationEvent event = NotificationEvent.builder()
					.eventType(NotificationEventType.PAYMENT_SUCCESS.name()).orderId(payment.getOrderId())
					.customerId(payment.getCustomerId()).message("Payment completed successfully.")
					.timestamp(java.time.LocalDateTime.now()).build();

			notificationPublisher.publish(event);
			System.out.println("Order Service updated successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return mapToResponse(payment);
	}
}