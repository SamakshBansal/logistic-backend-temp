package com.logistics.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyPaymentRequest {

	@NotBlank
	private String gatewayOrderId;

	@NotBlank
	private String gatewayPaymentId;

	@NotBlank
	private String signature;
}