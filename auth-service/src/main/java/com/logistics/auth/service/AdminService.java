package com.logistics.auth.service;

import java.util.List;

import com.logistics.auth.dto.request.CreateUserRequest;
import com.logistics.auth.dto.response.DeliveryAgentResponse;

public interface AdminService {

	void createDeliveryAgent(CreateUserRequest request);

	void createAdmin(CreateUserRequest request);
	
	List<DeliveryAgentResponse> getAllDeliveryAgents();

}