package com.logistics.order.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.order.dto.response.AdminDashboardResponse;
import com.logistics.order.dto.response.ApiResponse;
import com.logistics.order.dto.response.OrderResponse;
import com.logistics.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

	private final OrderService orderService;

	@GetMapping("/orders")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		System.out.println(authentication.getAuthorities());

		List<OrderResponse> orders = orderService.getAllOrders();

		ApiResponse<List<OrderResponse>> response = ApiResponse.<List<OrderResponse>>builder().success(true)
				.message("All orders fetched successfully").data(orders).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.ok(response);
	}

	@GetMapping("/orders/{orderId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable UUID orderId) {

		OrderResponse order = orderService.getOrderById(orderId);

		ApiResponse<OrderResponse> response = ApiResponse.<OrderResponse>builder().success(true)
				.message("Order fetched successfully").data(order).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.ok(response);
	}

	@GetMapping("/dashboard")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<AdminDashboardResponse>> getDashboard() {

		AdminDashboardResponse dashboard = orderService.getDashboard();

		ApiResponse<AdminDashboardResponse> response = ApiResponse.<AdminDashboardResponse>builder().success(true)
				.message("Dashboard fetched successfully").data(dashboard).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.ok(response);
	}

}