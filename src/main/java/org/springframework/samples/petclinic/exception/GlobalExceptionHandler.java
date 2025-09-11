package org.springframework.samples.petclinic.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.samples.petclinic.constants.ApiResponseConstants;
import org.springframework.samples.petclinic.model.ApiError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(PetIdNotFoundException.class)
	public ResponseEntity<ApiError> handlePetIdNotFound(PetIdNotFoundException petIdNotFoundException) {
		ApiError error = new ApiError(ApiResponseConstants.ERROR_PET_ID_NOT_FOUND, petIdNotFoundException.getMessage(),
			LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(PetIDExistsException.class)
	public ResponseEntity<ApiError> handlePetIdExists(PetIDExistsException petIDExistsException) {
		ApiError error = new ApiError(ApiResponseConstants.ERROR_PET_ID_ALREADY_EXISTS,
			petIDExistsException.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException dataIntegrityViolationException) {
		ApiError error = new ApiError(ApiResponseConstants.ERROR_CONSTRAINT_VIOLATION,
			dataIntegrityViolationException.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleGenericException(Exception e) {
		ApiError error = new ApiError(ApiResponseConstants.ERROR_SERVER_ERROR, e.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiError> InvalidArgumentException(Exception e) {
		ApiError error = new ApiError(ApiResponseConstants.ERROR_INVALID_INPUT, e.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

}
