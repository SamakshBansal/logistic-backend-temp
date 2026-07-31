package com.logistics.payment.dto.request;

import com.logistics.payment.enums.PaymentStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePaymentStatusRequest {

	private PaymentStatus paymentStatus;

}