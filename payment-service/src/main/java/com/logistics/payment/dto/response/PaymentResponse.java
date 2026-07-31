package com.logistics.payment.dto.response;

import com.logistics.payment.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

	private UUID paymentId;

	private UUID orderId;

	private UUID customerId;

	private BigDecimal amount;

	private String currency;

	private PaymentStatus paymentStatus;

	private String paymentMethod;

	private String paymentLink;

	private String gatewayOrderId; 
	
	private String gatewayPaymentId;

	private LocalDateTime createdAt;
	
	private String razorpayKey;
}