package com.logistics.payment.dto.request;

import com.logistics.payment.dto.response.Payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RazorpayWebhookRequest {

	private String event;

	private Payload payload;
}