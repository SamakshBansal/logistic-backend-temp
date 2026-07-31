package com.logistics.order.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.logistics.order.enums.OrderStatus;
import com.logistics.order.enums.PackageType;
import com.logistics.order.enums.PaymentStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {

    private UUID id;

    private String trackingNumber;

    private UUID customerId;

    private String pickupAddress;

    private String deliveryAddress;

    private String receiverName;

    private String receiverPhone;

    private PackageType packageType;

    private Double weight;
    
    private BigDecimal amount;

    private PaymentStatus paymentStatus;

    private OrderStatus orderStatus;

    private UUID assignedAgentId;

    private LocalDateTime createdAt;

}