package com.logistics.payment.service;

import com.logistics.payment.dto.request.CreatePaymentRequest;
import com.logistics.payment.dto.request.VerifyPaymentRequest;
import com.logistics.payment.dto.response.PaymentResponse;

public interface PaymentService {

	PaymentResponse createPayment(CreatePaymentRequest request);

	PaymentResponse verifyPayment(VerifyPaymentRequest request);

}