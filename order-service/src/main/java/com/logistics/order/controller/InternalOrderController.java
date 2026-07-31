package com.logistics.order.controller;

import com.logistics.order.dto.request.UpdatePaymentStatusRequest;
import com.logistics.order.dto.response.OrderPaymentDetailsResponse;
import com.logistics.order.dto.response.OrderResponse;
import com.logistics.order.service.OrderService;

//import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/internal/orders")
@RequiredArgsConstructor
public class InternalOrderController {

	private final OrderService orderService;

	@GetMapping("/{orderId}/payment-details")
	public OrderPaymentDetailsResponse getPaymentDetails(@PathVariable UUID orderId) {

		return orderService.getPaymentDetails(orderId);
	}

	@PostMapping("/{orderId}/payment-status")
	public ResponseEntity<OrderResponse> updatePaymentStatus(@PathVariable UUID orderId,
			@RequestBody UpdatePaymentStatusRequest request) {

		return ResponseEntity.ok(orderService.updatePaymentStatus(orderId, request));
	}
}