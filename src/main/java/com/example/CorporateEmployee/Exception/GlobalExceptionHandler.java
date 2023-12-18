package com.example.CorporateEmployee.Exception;

import java.net.http.HttpHeaders;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception) {

		// creating a new object of ErrorDetails and initializing it ErrorDetails
		ErrorDetails errorDetails = new ErrorDetails();
		errorDetails.setTimestamp(LocalDateTime.now());
		errorDetails.setStatusCode(HttpStatus.NOT_FOUND.value());
		errorDetails.setError("An error occurred: " + exception.getMessage());
		errorDetails.setStatus(HttpStatus.NOT_FOUND.toString());
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(CustomInvalidFileFormatException.class)
	public ResponseEntity<ErrorDetails> handleCustomInvalidFileFormatException(CustomInvalidFileFormatException exception) {

		// creating a new object of ErrorDetails and initializing it ErrorDetails
		ErrorDetails errorDetails = new ErrorDetails();
		errorDetails.setTimestamp(LocalDateTime.now());
		errorDetails.setStatusCode(HttpStatus.BAD_REQUEST.value());
		errorDetails.setError("An error occurred: The file expected in.csv Format  " + exception.getMessage());
		errorDetails.setStatus(HttpStatus.BAD_REQUEST.toString());
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(DateFormatException.class)
	public ResponseEntity<ErrorDetails> handleDataFormatException(DateFormatException exception) {

		ErrorDetails errorDetails = new ErrorDetails();
		errorDetails.setTimestamp(LocalDateTime.now());
		errorDetails.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		errorDetails.setError("An error occurred: " + exception.getMessage());
		errorDetails.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception) {

		ErrorDetails errorDetails = new ErrorDetails();
		errorDetails.setTimestamp(LocalDateTime.now());
		errorDetails.setStatusCode(HttpStatus.BAD_REQUEST.value());
		errorDetails.setError("An error occurred: " + exception.getMessage());
		errorDetails.setStatus(HttpStatus.BAD_REQUEST.toString());
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })

	protected ResponseEntity<ErrorDetails> handleConflict(RuntimeException ex) {

		ErrorDetails errorDetails = new ErrorDetails();
		errorDetails.setTimestamp(LocalDateTime.now());
		errorDetails.setStatusCode(HttpStatus.CONFLICT.value());
		errorDetails.setError("An error occurred: " + ex.getMessage());
		errorDetails.setStatus(HttpStatus.CONFLICT.toString());
		return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);

	}
	
	@ExceptionHandler(BadUserLoginDetailsException.class)
	public ResponseEntity<ErrorDetails> handleBadCredentialsException(Exception exception) {

		ErrorDetails errorDetails = new ErrorDetails();
		errorDetails.setTimestamp(LocalDateTime.now());
		errorDetails.setStatusCode(HttpStatus.BAD_REQUEST.value());
		errorDetails.setError("An error occurred due to INVALID_CREDENTIALS : " + exception.getMessage());
		errorDetails.setStatus(HttpStatus.BAD_REQUEST.toString());
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}


	@ExceptionHandler(NullPointerException.class) // exception handled public
	ResponseEntity<ErrorDetails> handleNullPointerExceptions(Exception e) { //

		ErrorDetails errorDetails = new ErrorDetails();
		errorDetails.setTimestamp(LocalDateTime.now());
		errorDetails.setStatusCode(HttpStatus.BAD_REQUEST.value());
		errorDetails.setError("An error occurred: " + e.getMessage());
		errorDetails.setStatus(HttpStatus.BAD_REQUEST.toString());
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);

	}

}
