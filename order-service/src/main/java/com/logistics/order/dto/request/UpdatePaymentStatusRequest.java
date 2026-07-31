package com.logistics.order.dto.request;

import com.logistics.order.enums.PaymentStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePaymentStatusRequest {

	@NotNull
	private PaymentStatus paymentStatus;
}