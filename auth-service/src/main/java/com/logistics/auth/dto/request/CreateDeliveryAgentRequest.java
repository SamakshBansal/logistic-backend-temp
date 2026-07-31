package com.logistics.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateDeliveryAgentRequest {

	@NotBlank(message = "Name is required")
	@Size(min = 3, max = 50)
	private String name;

	@NotBlank(message = "Email is required")
	@Email
	private String email;

	@NotBlank(message = "Password is required")
	@Size(min = 8)
	private String password;
}