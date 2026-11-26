package com.example.employeemanagement.exception;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice // handles exception globally across REST controllers
public class GlobalExceptionHandler {

	@ExceptionHandler(EmployeeNotFoundException.class) // specifies which exception this method handles
	public ResponseEntity<Map<String, Object>> handleEmployeeNotFound(EmployeeNotFoundException ex) {

		Map<String, Object> errorResponse = Map.of("message", ex.getMessage(), "status", HttpStatus.NOT_FOUND.value());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	} // ResponseEntity - handles HttpStatus and response body

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
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

		Map<String, Object> response = Map.of("status", HttpStatus.BAD_REQUEST.value(), "errors", errors);

		// HttpStatus.BAD_REQUEST.value() returns 400
		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {

		Map<String, Object> errorResponse = Map.of("status", ex.getStatusCode().value(), "message",
				ex.getReason() != null ? ex.getReason() : "Request could not be processed");

		return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
	}

	/**
	 * Why use Map<String, Object> instead of Map<String, String>?
	 * 
	 * Because the values have different data types: - status is an Integer. -
	 * errors is another Map. Therefore, Object accommodates both.
	 */
}
