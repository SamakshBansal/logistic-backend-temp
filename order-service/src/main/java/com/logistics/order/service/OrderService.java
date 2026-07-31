package com.logistics.order.service;

import java.util.List;
import java.util.UUID;

import com.logistics.order.dto.request.AssignAgentRequest;
import com.logistics.order.dto.request.CreateOrderRequest;
import com.logistics.order.dto.request.UpdateOrderStatusRequest;
import com.logistics.order.dto.request.UpdatePaymentStatusRequest;
import com.logistics.order.dto.response.AdminDashboardResponse;
import com.logistics.order.dto.response.OrderPaymentDetailsResponse;
import com.logistics.order.dto.response.OrderResponse;

public interface OrderService {

	OrderResponse createOrder(UUID customerId, CreateOrderRequest request);

	OrderResponse getOrderById(UUID orderId);

	List<OrderResponse> getOrdersByCustomer(UUID customerId);

	OrderResponse updateOrderStatus(UUID orderId, UpdateOrderStatusRequest request);

	OrderResponse cancelOrder(UUID orderId);

	OrderResponse assignDeliveryAgent(UUID orderId, AssignAgentRequest request);

	OrderResponse getOrderByTrackingNumber(String trackingNumber);

	OrderPaymentDetailsResponse getPaymentDetails(UUID orderId);

	OrderResponse updatePaymentStatus(UUID orderId, UpdatePaymentStatusRequest request);

	List<OrderResponse> getAllOrders();

	List<OrderResponse> getAssignedOrders(UUID agentId);

	OrderResponse updateOrderStatusByAgent(UUID agentId, UUID orderId, UpdateOrderStatusRequest request);

	AdminDashboardResponse getDashboard();
}