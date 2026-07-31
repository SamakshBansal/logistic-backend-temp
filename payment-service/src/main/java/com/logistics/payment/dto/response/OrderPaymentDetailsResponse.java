package com.logistics.payment.dto.response;

import com.logistics.payment.enums.OrderStatus;
import com.logistics.payment.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPaymentDetailsResponse {

	private UUID orderId;

	private UUID customerId;

	private BigDecimal amount;

	private OrderStatus orderStatus;

	private PaymentStatus paymentStatus;

}