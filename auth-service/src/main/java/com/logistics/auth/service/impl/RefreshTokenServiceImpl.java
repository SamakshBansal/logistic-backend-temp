package com.logistics.auth.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.logistics.auth.entity.RefreshToken;
import com.logistics.auth.entity.User;
import com.logistics.auth.exception.RefreshTokenException;
import com.logistics.auth.repository.RefreshTokenRepository;
import com.logistics.auth.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;

	@Override
	public RefreshToken createRefreshToken(User user) {

		RefreshToken refreshToken = refreshTokenRepository.findByUser(user).orElse(new RefreshToken());

		refreshToken.setUser(user);

		refreshToken.setToken(UUID.randomUUID().toString());

		refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));

		refreshToken.setRevoked(false);

		return refreshTokenRepository.save(refreshToken);

	}

	@Override
	public RefreshToken verifyRefreshToken(String token) {

		RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
				.orElseThrow(() -> new RefreshTokenException("Refresh token not found"));

		if (refreshToken.isRevoked()) {
			throw new RefreshTokenException("Refresh token has been revoked");
		}

		if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
			throw new RefreshTokenException("Refresh token has expired");
		}

		return refreshToken;
	}

	@Override
	public void revokeRefreshToken(String token) {

		RefreshToken refreshToken = verifyRefreshToken(token);

		refreshToken.setRevoked(true);

		refreshTokenRepository.save(refreshToken);
	}

}