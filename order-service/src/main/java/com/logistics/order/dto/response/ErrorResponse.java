package com.logistics.order.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

	private boolean success;

	private String errorCode;

	private String message;

	private LocalDateTime timestamp;

}