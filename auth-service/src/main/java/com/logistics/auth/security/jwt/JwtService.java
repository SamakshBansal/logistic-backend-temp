package com.logistics.auth.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.logistics.auth.entity.User;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private long expiration;

	private SecretKey getSigningKey() {

		byte[] keyBytes = Decoders.BASE64.decode(secret);

		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateToken(UserDetails userDetails, User user) {

		return Jwts.builder()

				.subject(userDetails.getUsername())

				.claim("userId", user.getId())

				.claim("email", user.getEmail())

				.claim("role", user.getRole().name())

				.issuedAt(new Date())

				.expiration(new Date(System.currentTimeMillis() + expiration))

				.signWith(getSigningKey())

				.compact();
	}

	private Claims extractAllClaims(String token) {

		return Jwts.parser()

				.verifyWith(getSigningKey())

				.build()

				.parseSignedClaims(token)

				.getPayload();
	}

	public String extractUsername(String token) {
		return extractAllClaims(token).getSubject();
	}

	public UUID extractUserId(String token) {
		String userId = extractAllClaims(token).get("userId", String.class);

		return UUID.fromString(userId);
	}

	public String extractRole(String token) {
		return extractAllClaims(token).get("role", String.class);
	}

	public String extractEmail(String token) {
		return extractAllClaims(token).get("email", String.class);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractAllClaims(token).getExpiration();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {

		return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

}