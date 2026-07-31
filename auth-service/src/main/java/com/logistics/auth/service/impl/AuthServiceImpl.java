package com.logistics.auth.service.impl;

import com.logistics.auth.dto.request.LoginRequest;
import com.logistics.auth.dto.request.RefreshTokenRequest;
import com.logistics.auth.dto.request.RegisterRequest;
import com.logistics.auth.dto.response.AuthResponse;
import com.logistics.auth.entity.RefreshToken;
import com.logistics.auth.entity.User;
import com.logistics.auth.enums.Role;
import com.logistics.auth.exception.ResourceNotFoundException;
import com.logistics.auth.exception.UserAlreadyExistsException;
import com.logistics.auth.repository.UserRepository;
import com.logistics.auth.security.jwt.JwtService;
import com.logistics.auth.security.userdetails.CustomUserDetailsService;
import com.logistics.auth.service.AuthService;
import com.logistics.auth.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final RefreshTokenService refreshTokenService;
	private final CustomUserDetailsService customUserDetailsService;

	@Override
	public AuthResponse login(LoginRequest request) {

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		String accessToken = jwtService.generateToken(userDetails, user);

		RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

		return AuthResponse.builder().accessToken(accessToken).refreshToken(refreshToken.getToken()).tokenType("Bearer")
				.userId(user.getId()).name(user.getName()).email(user.getEmail()).role(user.getRole().name()).build();
	}

	@Override
	public AuthResponse refreshToken(RefreshTokenRequest request) {

		RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(request.getRefreshToken());

		User user = refreshToken.getUser();

		UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());

		String accessToken = jwtService.generateToken(userDetails, user);

		return AuthResponse.builder().accessToken(accessToken).refreshToken(refreshToken.getToken()).tokenType("Bearer")
				.userId(user.getId()).name(user.getName()).email(user.getEmail()).role(user.getRole().name()).build();
	}

	@Override
	public void logout(RefreshTokenRequest request) {
		refreshTokenService.revokeRefreshToken(request.getRefreshToken());
	}

	@Override
	public void register(RegisterRequest request) {

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new UserAlreadyExistsException("Email address is already in use: " + request.getEmail());
		}

		User user = User.builder().name(request.getName()).email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword())).role(Role.CUSTOMER).enabled(true).build();

		userRepository.save(user);
	}

}