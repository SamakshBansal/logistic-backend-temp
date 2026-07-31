package com.logistics.payment.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.logistics.payment.dto.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(PaymentNotFoundException.class)
	public ResponseEntity<ApiResponse<Void>> handlePaymentNotFound(PaymentNotFoundException ex) {

		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(ApiResponse.error("PAYMENT_NOT_FOUND", ex.getMessage()));
	}

	@ExceptionHandler(PaymentAlreadyExistsException.class)
	public ResponseEntity<ApiResponse<Void>> handlePaymentAlreadyExists(PaymentAlreadyExistsException ex) {

		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(ApiResponse.error("PAYMENT_ALREADY_EXISTS", ex.getMessage()));
	}

	@ExceptionHandler(PaymentVerificationException.class)
	public ResponseEntity<ApiResponse<Void>> handleVerification(PaymentVerificationException ex) {

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ApiResponse.error("PAYMENT_VERIFICATION_FAILED", ex.getMessage()));
	}

	@ExceptionHandler(PaymentGatewayException.class)
	public ResponseEntity<ApiResponse<Void>> handleGateway(PaymentGatewayException ex) {

		return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
				.body(ApiResponse.error("PAYMENT_GATEWAY_ERROR", ex.getMessage()));
	}

	@ExceptionHandler(InvalidPaymentStateException.class)
	public ResponseEntity<ApiResponse<Void>> handleInvalidState(InvalidPaymentStateException ex) {

		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(ApiResponse.error("INVALID_PAYMENT_STATE", ex.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {

		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult().getAllErrors().forEach(error -> {
			String field = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			errors.put(field, message);
		});

		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {

		ex.printStackTrace();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(ApiResponse.error("INTERNAL_SERVER_ERROR", ex.getMessage()));
	}
}