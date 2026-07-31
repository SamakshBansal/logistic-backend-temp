package com.logistics.payment.service;

public interface WebhookService {

	void processWebhook(String payload, String signature);

}