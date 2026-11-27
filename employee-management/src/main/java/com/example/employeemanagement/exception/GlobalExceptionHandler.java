package com.example.employeemanagement.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import com.example.employeemanagement.dto.ErrorResponse;

@RestControllerAdvice // handles exception globally across REST controllers
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(EmployeeNotFoundException.class) // specifies which exception this method handles
	public ResponseEntity<ErrorResponse> handleEmployeeNotFound(EmployeeNotFoundException ex, WebRequest request) {

		logger.warn("Employee not found: {}", ex.getMessage());

		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(),
				HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage(),
				request.getDescription(false).replace("uri=", ""), null);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	} // ResponseEntity - handles HttpStatus and response body

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex,
			WebRequest request) {
		Map<String, String> errors = ex.getBindingResult() // gets validation results
				.getFieldErrors() // get field errors
				.stream() // list of field errors into a stream
				.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage,
						(existing, replacement) -> existing, LinkedHashMap::new)); // Convert the stream into map. Map
																					// contains key-value pair. Key =
																					// Field name, value = validation
																					// error message
		// (existing,replacement) -> existing, - keeps the first message if a field has
		// multiple errors
		// LinkedHashMap::new - Creates a LinkedHashMap, preserving insertion order
		logger.warn("Validation failed: {}", errors);
		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.getReasonPhrase(), "Validation failed",
				request.getDescription(false).replace("uri=", ""), errors);

		// HttpStatus.BAD_REQUEST.value() returns 400
		return ResponseEntity.badRequest().body(errorResponse);
	}

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {

		logger.warn("Request failed: {}", ex.getReason());
		HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
		ErrorResponse errorResponse = new ErrorResponse(
						LocalDateTime.now(), 
						status.value(), 
						status.getReasonPhrase(),
						ex.getReason() != null ? ex.getReason() : "Request could not be processed",
						request.getDescription(false).replace("uri=", ""), 
						null);

		return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, WebRequest request) {

		logger.error("Unexpected application error", ex);

		ErrorResponse errorResponse = new ErrorResponse(
								LocalDateTime.now(), 
								HttpStatus.INTERNAL_SERVER_ERROR.value(),
								HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), 
								"An unexpected error occurred",
								request.getDescription(false).replace("uri=", ""), 
								null);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
	/**
	 * Why use Map<String, Object> instead of Map<String, String>?
	 * 
	 * Because the values have different data types: - status is an Integer. -
	 * errors is another Map. Therefore, Object accommodates both.
	 */
}
