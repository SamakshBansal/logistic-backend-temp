package com.logistics.payment.client;

import com.logistics.payment.dto.request.UpdatePaymentStatusRequest;
import com.logistics.payment.dto.response.OrderPaymentDetailsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "order-service")
public interface OrderClient {

	@GetMapping("/api/v1/internal/orders/{orderId}/payment-details")
	OrderPaymentDetailsResponse getPaymentDetails(@PathVariable UUID orderId);

	@PostMapping("/api/v1/internal/orders/{orderId}/payment-status")
	void updatePaymentStatus(@PathVariable UUID orderId, @RequestBody UpdatePaymentStatusRequest request);

}