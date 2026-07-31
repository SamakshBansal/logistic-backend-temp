package com.logistics.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {

	private String eventType;

	private UUID orderId;

	private UUID customerId;

	private String message;

	private LocalDateTime timestamp;
}