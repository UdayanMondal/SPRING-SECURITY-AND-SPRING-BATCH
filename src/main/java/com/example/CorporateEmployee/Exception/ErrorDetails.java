package com.example.CorporateEmployee.Exception;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import java.net.http.HttpHeaders;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public class ErrorDetails {

	private int  statusCode;
	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	private HttpHeaders statusHeader;

	private String status;
	private String error;
	private String path;
	private LocalDateTime timestamp;

	/*
	 * public ErrorDetails(HttpStatusCode statusCode, HttpHeaders statusHeader,
	 * String status, String error, String path, LocalDateTime timestamp) { super();
	 * this.statusCode = statusCode; this.statusHeader = statusHeader; this.status =
	 * status; this.error = error; this.path = path; this.timestamp = timestamp; }
	 */

	

	public HttpHeaders getStatusHeader() {
		return statusHeader;
	}

	public void setStatusHeader(HttpHeaders statusHeader) {
		this.statusHeader = statusHeader;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
