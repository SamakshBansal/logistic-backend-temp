package com.logistics.order.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.logistics.order.dto.request.AssignAgentRequest;
import com.logistics.order.dto.request.CreateOrderRequest;
import com.logistics.order.dto.request.UpdateOrderStatusRequest;
import com.logistics.order.dto.request.UpdatePaymentStatusRequest;
import com.logistics.order.dto.response.AdminDashboardResponse;
import com.logistics.order.dto.response.OrderPaymentDetailsResponse;
import com.logistics.order.dto.response.OrderResponse;
import com.logistics.order.entity.Order;
import com.logistics.order.enums.OrderStatus;
import com.logistics.order.enums.PaymentStatus;
import com.logistics.order.exception.InvalidOrderStateException;
import com.logistics.order.exception.OrderNotFoundException;
import com.logistics.order.repository.OrderRepository;
import com.logistics.order.service.OrderService;
import com.logistics.order.service.PricingService;
import com.logistics.order.util.TrackingNumberGenerator;

import com.logistics.order.config.RabbitMQConfig;
import com.logistics.order.dto.NotificationEvent;
import com.logistics.order.enums.NotificationEventType;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final PricingService pricingService;
	private final RabbitTemplate rabbitTemplate;

	private OrderResponse mapToResponse(Order order) {

		return OrderResponse.builder().id(order.getId()).trackingNumber(order.getTrackingNumber())
				.customerId(order.getCustomerId()).pickupAddress(order.getPickupAddress())
				.deliveryAddress(order.getDeliveryAddress()).receiverName(order.getReceiverName())
				.receiverPhone(order.getReceiverPhone()).packageType(order.getPackageType()).weight(order.getWeight())
				.amount(order.getAmount()).paymentStatus(order.getPaymentStatus()).orderStatus(order.getOrderStatus())
				.assignedAgentId(order.getAssignedAgentId()).createdAt(order.getCreatedAt()).build();
	}

	@Override
	public OrderResponse createOrder(UUID customerId, CreateOrderRequest request) {

		BigDecimal amount = pricingService.calculatePrice(request.getPackageType(), request.getWeight());

		Order order = Order.builder()

				.customerId(customerId)

				.trackingNumber(TrackingNumberGenerator.generateTrackingNumber())

				.pickupAddress(request.getPickupAddress())

				.deliveryAddress(request.getDeliveryAddress())

				.receiverName(request.getReceiverName())

				.receiverPhone(request.getReceiverPhone())

				.packageType(request.getPackageType())

				.weight(request.getWeight())

				.amount(amount)

				.paymentStatus(PaymentStatus.PENDING)

				.orderStatus(OrderStatus.CREATED)

				.build();

		Order savedOrder = orderRepository.save(order);

		NotificationEvent event = NotificationEvent.builder().eventType(NotificationEventType.ORDER_CREATED.name())
				.orderId(savedOrder.getId()).customerId(savedOrder.getCustomerId())
				.message("Your order has been created successfully.").timestamp(LocalDateTime.now()).build();

		rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, event);

		System.out.println("ORDER_CREATED event published");

		return mapToResponse(savedOrder);
	}

	@Override
	public OrderResponse getOrderById(UUID orderId) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

		return mapToResponse(order);
	}

	@Override
	public List<OrderResponse> getOrdersByCustomer(UUID customerId) {

		List<Order> orders = orderRepository.findByCustomerId(customerId);

		return orders.stream().map(this::mapToResponse).toList();
	}

	@Override
	public OrderResponse updateOrderStatus(UUID orderId, UpdateOrderStatusRequest request) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

		order.setOrderStatus(request.getOrderStatus());

		Order updatedOrder = orderRepository.save(order);

		if (updatedOrder.getOrderStatus() == OrderStatus.PICKED_UP) {

			NotificationEvent event = NotificationEvent.builder()
					.eventType(NotificationEventType.ORDER_PICKED_UP.name()).orderId(updatedOrder.getId())
					.customerId(updatedOrder.getCustomerId()).message("Your package has been picked up.")
					.timestamp(LocalDateTime.now()).build();

			rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, event);

			System.out.println("ORDER_PICKED_UP event published");
		}

		if (updatedOrder.getOrderStatus() == OrderStatus.DELIVERED) {

			NotificationEvent event = NotificationEvent.builder()
					.eventType(NotificationEventType.ORDER_DELIVERED.name()).orderId(updatedOrder.getId())
					.customerId(updatedOrder.getCustomerId()).message("Your order has been delivered successfully.")
					.timestamp(LocalDateTime.now()).build();

			rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, event);

			System.out.println("ORDER_DELIVERED event published");
		}

		return mapToResponse(updatedOrder);
	}

	@Override
	public OrderResponse cancelOrder(UUID orderId) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

		if (order.getOrderStatus() == OrderStatus.DELIVERED) {
			throw new InvalidOrderStateException("Delivered order cannot be cancelled.");
		}

		if (order.getOrderStatus() == OrderStatus.CANCELLED) {
			throw new InvalidOrderStateException("Order is already cancelled.");
		}

		order.setOrderStatus(OrderStatus.CANCELLED);

		Order updatedOrder = orderRepository.save(order);

		return mapToResponse(updatedOrder);
	}

	@Override
	public OrderResponse assignDeliveryAgent(UUID orderId, AssignAgentRequest request) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

		if (order.getOrderStatus() == OrderStatus.CANCELLED) {
			throw new InvalidOrderStateException("Cancelled order cannot be assigned.");
		}

		if (order.getOrderStatus() == OrderStatus.DELIVERED) {
			throw new InvalidOrderStateException("Delivered order cannot be assigned.");
		}

		if (order.getAssignedAgentId() != null) {
			throw new InvalidOrderStateException("Order is already assigned to a delivery agent.");
		}

		order.setAssignedAgentId(request.getAgentId());

		order.setOrderStatus(OrderStatus.ASSIGNED);

		Order updatedOrder = orderRepository.save(order);

		NotificationEvent event = NotificationEvent.builder().eventType(NotificationEventType.ORDER_ASSIGNED.name())
				.orderId(updatedOrder.getId()).customerId(updatedOrder.getCustomerId())
				.message("Your order has been assigned to a delivery agent.").timestamp(LocalDateTime.now()).build();

		rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, event);

		System.out.println("ORDER_ASSIGNED event published");

		return mapToResponse(updatedOrder);
	}

	@Override
	public OrderResponse getOrderByTrackingNumber(String trackingNumber) {

		Order order = orderRepository.findByTrackingNumber(trackingNumber).orElseThrow(
				() -> new OrderNotFoundException("Order not found with tracking number: " + trackingNumber));

		return mapToResponse(order);
	}

	@Override
	public OrderPaymentDetailsResponse getPaymentDetails(UUID orderId) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException("Order not found"));

		return OrderPaymentDetailsResponse.builder().orderId(order.getId()).customerId(order.getCustomerId())
				.amount(order.getAmount()).orderStatus(order.getOrderStatus()).paymentStatus(order.getPaymentStatus())
				.build();
	}

	@Override
	public OrderResponse updatePaymentStatus(UUID orderId, UpdatePaymentStatusRequest request) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException("Order not found"));

		order.setPaymentStatus(request.getPaymentStatus());

		Order updated = orderRepository.save(order);

		return mapToResponse(updated);
	}

	@Override
	public List<OrderResponse> getAllOrders() {

		return orderRepository.findAll().stream().map(this::mapToResponse).toList();
	}

	@Override
	public List<OrderResponse> getAssignedOrders(UUID agentId) {

		return orderRepository.findByAssignedAgentId(agentId).stream().map(this::mapToResponse).toList();
	}

	@Override
	public OrderResponse updateOrderStatusByAgent(UUID agentId, UUID orderId, UpdateOrderStatusRequest request) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException("Order not found"));

		if (order.getAssignedAgentId() == null || !order.getAssignedAgentId().equals(agentId)) {

			throw new RuntimeException("You are not assigned to this order");
		}

		return updateOrderStatus(orderId, request);
	}

	@Override
	public AdminDashboardResponse getDashboard() {

		return AdminDashboardResponse.builder()

				.totalOrders(orderRepository.count())

				.pendingPayments(orderRepository.countByPaymentStatus(PaymentStatus.PENDING))

				.successfulPayments(orderRepository.countByPaymentStatus(PaymentStatus.SUCCESS))

				.createdOrders(orderRepository.countByOrderStatus(OrderStatus.CREATED))

				.assignedOrders(orderRepository.countByOrderStatus(OrderStatus.ASSIGNED))

				.pickedUpOrders(orderRepository.countByOrderStatus(OrderStatus.PICKED_UP))

				.inTransitOrders(orderRepository.countByOrderStatus(OrderStatus.IN_TRANSIT))

				.deliveredOrders(orderRepository.countByOrderStatus(OrderStatus.DELIVERED))

				.cancelledOrders(orderRepository.countByOrderStatus(OrderStatus.CANCELLED))

				.build();
	}

}