package com.logistics.order.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AssignAgentRequest {

	@NotNull(message = "Agent ID is required")
	private UUID agentId;

}