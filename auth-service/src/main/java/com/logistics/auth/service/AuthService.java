package com.logistics.auth.service;

import com.logistics.auth.dto.request.LoginRequest;
import com.logistics.auth.dto.request.RegisterRequest;
import com.logistics.auth.dto.response.AuthResponse;
import com.logistics.auth.dto.request.RefreshTokenRequest;

public interface AuthService {

	void register(RegisterRequest request);

	AuthResponse login(LoginRequest request);

	AuthResponse refreshToken(RefreshTokenRequest request);

	void logout(RefreshTokenRequest request);


}