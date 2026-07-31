package com.logistics.payment.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import com.logistics.payment.config.RazorpayProperties;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RazorpaySignatureUtil {

	private final RazorpayProperties razorpayProperties;

	public boolean verifySignature(String gatewayOrderId, String gatewayPaymentId, String razorpaySignature) {

		try {

			String payload = gatewayOrderId + "|" + gatewayPaymentId;

			String generatedSignature = hmacSha256(payload, razorpayProperties.getKeySecret());

			return java.security.MessageDigest.isEqual(generatedSignature.getBytes(), razorpaySignature.getBytes());

		} catch (Exception e) {
			return false;
		}
	}

	private String hmacSha256(String data, String secret) throws Exception {

		Mac sha256 = Mac.getInstance("HmacSHA256");

		SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");

		sha256.init(secretKey);

		byte[] hash = sha256.doFinal(data.getBytes());

		StringBuilder hex = new StringBuilder();

		for (byte b : hash) {
			hex.append(String.format("%02x", b));
		}

		return hex.toString();
	}
}