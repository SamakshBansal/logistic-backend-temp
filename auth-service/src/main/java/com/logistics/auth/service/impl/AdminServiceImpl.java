package com.logistics.auth.service.impl;

import com.logistics.auth.dto.request.CreateUserRequest;
import com.logistics.auth.dto.response.DeliveryAgentResponse;
import com.logistics.auth.entity.User;
import com.logistics.auth.enums.Role;
import com.logistics.auth.exception.UserAlreadyExistsException;
import com.logistics.auth.repository.UserRepository;
import com.logistics.auth.service.AdminService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void createDeliveryAgent(CreateUserRequest request) {

		createUser(request, Role.DELIVERY_AGENT);
	}

	@Override
	public void createAdmin(CreateUserRequest request) {

		createUser(request, Role.ADMIN);
	}

	private void createUser(CreateUserRequest request, Role role) {

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new UserAlreadyExistsException("Email already exists.");
		}

		User user = User.builder().name(request.getName()).email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword())).role(role).enabled(true).build();

		userRepository.save(user);
	}
	
	@Override
	public List<DeliveryAgentResponse> getAllDeliveryAgents() {

	    return userRepository.findByRole(Role.DELIVERY_AGENT)
	            .stream()
	            .map(user -> DeliveryAgentResponse.builder()
	                    .id(user.getId())
	                    .name(user.getName())
	                    .email(user.getEmail())
	                    .build())
	            .toList();
	}
}