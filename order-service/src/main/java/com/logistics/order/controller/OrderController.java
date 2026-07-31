package com.logistics.order.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.logistics.order.dto.request.AssignAgentRequest;
import com.logistics.order.dto.request.CreateOrderRequest;
import com.logistics.order.dto.request.UpdateOrderStatusRequest;
import com.logistics.order.dto.response.ApiResponse;
import com.logistics.order.dto.response.OrderResponse;
import com.logistics.order.security.AuthenticatedUser;
import com.logistics.order.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@Valid @RequestBody CreateOrderRequest request) {

		AuthenticatedUser user = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		UUID customerId = user.getUserId();

		OrderResponse response = orderService.createOrder(customerId, request);

		ApiResponse<OrderResponse> apiResponse = ApiResponse.<OrderResponse>builder().success(true)
				.message("Order created successfully").data(response).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable UUID orderId) {

		OrderResponse response = orderService.getOrderById(orderId);

		ApiResponse<OrderResponse> apiResponse = ApiResponse.<OrderResponse>builder().success(true)
				.message("Order fetched successfully").data(response).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.ok(apiResponse);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<OrderResponse>>> getMyOrders() {

		AuthenticatedUser user = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		UUID customerId = user.getUserId();

		List<OrderResponse> orders = orderService.getOrdersByCustomer(customerId);

		ApiResponse<List<OrderResponse>> response = ApiResponse.<List<OrderResponse>>builder().success(true)
				.message("Orders fetched successfully").data(orders).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.ok(response);
	}

	@PatchMapping("/{orderId}/status")
	public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(

			@PathVariable UUID orderId,

			@Valid @RequestBody UpdateOrderStatusRequest request) {

		OrderResponse response = orderService.updateOrderStatus(orderId, request);

		ApiResponse<OrderResponse> apiResponse = ApiResponse.<OrderResponse>builder().success(true)
				.message("Order status updated successfully").data(response).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.ok(apiResponse);
	}

	@PatchMapping("/{orderId}/cancel")
	public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(@PathVariable UUID orderId) {

		OrderResponse response = orderService.cancelOrder(orderId);

		ApiResponse<OrderResponse> apiResponse = ApiResponse.<OrderResponse>builder().success(true)
				.message("Order cancelled successfully").data(response).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.ok(apiResponse);
	}

	@PatchMapping("/{orderId}/assign")
	public ResponseEntity<ApiResponse<OrderResponse>> assignDeliveryAgent(

			@PathVariable UUID orderId,

			@Valid @RequestBody AssignAgentRequest request) {

		OrderResponse response = orderService.assignDeliveryAgent(orderId, request);

		ApiResponse<OrderResponse> apiResponse = ApiResponse.<OrderResponse>builder().success(true)
				.message("Delivery agent assigned successfully").data(response).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.ok(apiResponse);
	}

	@GetMapping("/tracking/{trackingNumber}")
	public ResponseEntity<ApiResponse<OrderResponse>> getOrderByTrackingNumber(@PathVariable String trackingNumber) {

		OrderResponse response = orderService.getOrderByTrackingNumber(trackingNumber);

		ApiResponse<OrderResponse> apiResponse = ApiResponse.<OrderResponse>builder().success(true)
				.message("Order tracking details fetched successfully").data(response).timestamp(LocalDateTime.now())
				.build();

		return ResponseEntity.ok(apiResponse);
	}

}