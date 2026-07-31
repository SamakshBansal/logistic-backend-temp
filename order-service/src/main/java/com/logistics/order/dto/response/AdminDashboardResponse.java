package com.logistics.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardResponse {

	private long totalOrders;

	private long pendingPayments;
	private long successfulPayments;

	private long createdOrders;
	private long assignedOrders;
	private long pickedUpOrders;
	private long inTransitOrders;
	private long deliveredOrders;
	private long cancelledOrders;
}
