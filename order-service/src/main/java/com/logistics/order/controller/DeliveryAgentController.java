package com.logistics.order.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.logistics.order.dto.request.UpdateOrderStatusRequest;
import com.logistics.order.dto.response.ApiResponse;
import com.logistics.order.dto.response.OrderResponse;
import com.logistics.order.security.AuthenticatedUser;
import com.logistics.order.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/agent")
@RequiredArgsConstructor
public class DeliveryAgentController {

	private final OrderService orderService;

	@GetMapping("/orders")
	@PreAuthorize("hasRole('DELIVERY_AGENT')")
	public ResponseEntity<ApiResponse<List<OrderResponse>>> getAssignedOrders() {

		AuthenticatedUser user = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		UUID agentId = user.getUserId();

		List<OrderResponse> orders = orderService.getAssignedOrders(agentId);

		ApiResponse<List<OrderResponse>> response = ApiResponse.<List<OrderResponse>>builder().success(true)
				.message("Assigned orders fetched successfully").data(orders).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.ok(response);
	}

	@PatchMapping("/orders/{orderId}/status")
	@PreAuthorize("hasRole('DELIVERY_AGENT')")
	public ResponseEntity<ApiResponse<OrderResponse>> updateStatus(

			@PathVariable UUID orderId,

			@Valid @RequestBody UpdateOrderStatusRequest request) {

		AuthenticatedUser user = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		OrderResponse response = orderService.updateOrderStatusByAgent(user.getUserId(), orderId, request);

		ApiResponse<OrderResponse> apiResponse = ApiResponse.<OrderResponse>builder().success(true)
				.message("Order status updated successfully").data(response).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.ok(apiResponse);
	}

}