/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.appointment;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for appointment-related exceptions.
 *
 * @author Spring PetClinic Team
 */
@RestControllerAdvice(basePackages = "org.springframework.samples.petclinic.appointment")
public class AppointmentExceptionHandler {

	/**
	 * Handle validation exceptions.
	 * @param ex the exception
	 * @return error response with validation details
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, Object> response = new HashMap<>();
		response.put("timestamp", LocalDateTime.now());
		response.put("status", HttpStatus.BAD_REQUEST.value());
		response.put("error", "Validation Failed");

		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});

		response.put("errors", errors);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	/**
	 * Handle data integrity violations (e.g., double booking).
	 * @param ex the exception
	 * @return error response with conflict details
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
		Map<String, Object> response = new HashMap<>();
		response.put("timestamp", LocalDateTime.now());
		response.put("status", HttpStatus.CONFLICT.value());
		response.put("error", "Conflict");
		response.put("message", ex.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}

	/**
	 * Handle illegal argument exceptions.
	 * @param ex the exception
	 * @return error response with bad request details
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
		Map<String, Object> response = new HashMap<>();
		response.put("timestamp", LocalDateTime.now());
		response.put("status", HttpStatus.BAD_REQUEST.value());
		response.put("error", "Bad Request");
		response.put("message", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

}