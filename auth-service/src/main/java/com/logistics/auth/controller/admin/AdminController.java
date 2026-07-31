package com.logistics.auth.controller.admin;

import com.logistics.auth.dto.request.CreateUserRequest;
import com.logistics.auth.dto.response.ApiResponse;
import com.logistics.auth.dto.response.DeliveryAgentResponse;
import com.logistics.auth.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;

	@PostMapping("/agents")
	@PreAuthorize("hasRole('ADMIN')")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<ApiResponse<Void>> createDeliveryAgent(@Valid @RequestBody CreateUserRequest request) {

		adminService.createDeliveryAgent(request);

		ApiResponse<Void> response = ApiResponse.<Void>builder().success(true).message("Delivery Agent registered successfully")
				.build();

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/admins")
	@PreAuthorize("hasRole('ADMIN')")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<ApiResponse<Void>> createAdmin(@Valid @RequestBody CreateUserRequest request) {

		adminService.createAdmin(request);

		ApiResponse<Void> response = ApiResponse.<Void>builder().success(true)
				.message("Admin registered successfully").build();

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping("/agents")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<List<DeliveryAgentResponse>>> getAllDeliveryAgents() {

	    List<DeliveryAgentResponse> agents = adminService.getAllDeliveryAgents();

	    ApiResponse<List<DeliveryAgentResponse>> response =
	            ApiResponse.<List<DeliveryAgentResponse>>builder()
	                    .success(true)
	                    .message("Delivery agents fetched successfully")
	                    .data(agents)
	                    .build();

	    return ResponseEntity.ok(response);
	}
}