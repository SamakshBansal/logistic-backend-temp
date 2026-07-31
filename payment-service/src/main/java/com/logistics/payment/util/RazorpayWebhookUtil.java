package com.logistics.payment.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import com.logistics.payment.config.RazorpayProperties;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RazorpayWebhookUtil {

	private final RazorpayProperties razorpayProperties;

	public boolean verify(String payload, String signature) {

		try {

			String generated = hmacSha256(payload, razorpayProperties.getKeySecret());

			return generated.equals(signature);

		} catch (Exception e) {
			return false;
		}

	}

	private String hmacSha256(String data, String secret) throws Exception {

		Mac sha256 = Mac.getInstance("HmacSHA256");

		SecretKeySpec key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");

		sha256.init(key);

		byte[] hash = sha256.doFinal(data.getBytes());

		StringBuilder sb = new StringBuilder();

		for (byte b : hash) {
			sb.append(String.format("%02x", b));
		}

		return sb.toString();

	}

}