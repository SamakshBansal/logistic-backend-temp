package com.logistics.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.payment.service.WebhookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/webhooks")
@RequiredArgsConstructor
public class WebhookController {

	private final WebhookService webhookService;

	@PostMapping("/razorpay")
	public ResponseEntity<Void> handleWebhook(

			@RequestBody String payload,

			@RequestHeader("X-Razorpay-Signature") String signature) {

		webhookService.processWebhook(payload, signature);

		return ResponseEntity.ok().build();
	}
}
