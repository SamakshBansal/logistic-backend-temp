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
public class PaymentHistoryResponse {

	private UUID paymentId;

	private UUID orderId;

	private BigDecimal amount;

	private PaymentStatus paymentStatus;

	private LocalDateTime createdAt;
}