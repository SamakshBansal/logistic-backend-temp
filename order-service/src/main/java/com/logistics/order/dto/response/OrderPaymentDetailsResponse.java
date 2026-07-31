package com.logistics.order.dto.response;

import com.logistics.order.enums.OrderStatus;
import com.logistics.order.enums.PaymentStatus;
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