package com.logistics.order.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.logistics.order.enums.OrderStatus;
import com.logistics.order.enums.PackageType;
import com.logistics.order.enums.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

	@Id
	private UUID id;

	@Column(nullable = false, unique = true)
	private String trackingNumber;

	@Column(nullable = false)
	private UUID customerId;

	@Column(nullable = false)
	private String pickupAddress;

	@Column(nullable = false)
	private String deliveryAddress;

	@Column(nullable = false)
	private String receiverName;

	@Column(nullable = false)
	private String receiverPhone;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PackageType packageType;

	@Column(nullable = false)
	private Double weight;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal amount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentStatus paymentStatus;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus orderStatus;

	private UUID assignedAgentId;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private LocalDateTime updatedAt;

	@PrePersist
	public void prePersist() {
		this.id = UUID.randomUUID();
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	public void preUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
}