package com.logistics.order.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.logistics.order.dto.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(OrderNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleOrderNotFound(OrderNotFoundException ex) {

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder().success(false)
				.errorCode("ORDER_NOT_FOUND").message(ex.getMessage()).timestamp(LocalDateTime.now()).build());
	}

	@ExceptionHandler(InvalidOrderStateException.class)
	public ResponseEntity<ErrorResponse> handleInvalidState(InvalidOrderStateException ex) {

		return ResponseEntity.badRequest().body(ErrorResponse.builder().success(false).errorCode("INVALID_ORDER_STATE")
				.message(ex.getMessage()).timestamp(LocalDateTime.now()).build());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {

		String message = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage()).collect(Collectors.joining(", "));

		return ResponseEntity.badRequest().body(ErrorResponse.builder().success(false).errorCode("VALIDATION_ERROR")
				.message(message).timestamp(LocalDateTime.now()).build());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.builder().success(false)
				.errorCode("INTERNAL_SERVER_ERROR").message(ex.getMessage()).timestamp(LocalDateTime.now()).build());
	}
}