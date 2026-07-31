package com.logistics.auth.controller;

import com.logistics.auth.dto.request.LoginRequest;
import com.logistics.auth.dto.request.RefreshTokenRequest;
import com.logistics.auth.dto.request.RegisterRequest;
import com.logistics.auth.dto.response.ApiResponse;
import com.logistics.auth.dto.response.AuthResponse;
import com.logistics.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest request) {

		authService.register(request);

		return ResponseEntity
				.ok(ApiResponse.<Void>builder().success(true).message("User registered successfully").build());
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {

		AuthResponse response = authService.login(request);

		return ResponseEntity.ok(

				ApiResponse.<AuthResponse>builder()

						.success(true)

						.message("Login successful")

						.data(response)

						.build()

		);

	}

	@PostMapping("/refresh")
	public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {

		AuthResponse response = authService.refreshToken(request);

		return ResponseEntity.ok(

				ApiResponse.<AuthResponse>builder()

						.success(true)

						.message("Token refreshed successfully")

						.data(response)

						.build()

		);

	}

	@PostMapping("/logout")
	public ResponseEntity<ApiResponse<Void>> logout(@Valid @RequestBody RefreshTokenRequest request) {

		authService.logout(request);

		return ResponseEntity.ok(

				ApiResponse.<Void>builder()

						.success(true)

						.message("Logged out successfully")

						.build()

		);

	}

	/*
	 * @GetMapping("/me") public String me(Authentication authentication) {
	 * 
	 * return authentication.getName();
	 * 
	 * }
	 */
}