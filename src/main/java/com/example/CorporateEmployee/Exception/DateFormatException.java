package com.example.CorporateEmployee.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST)
public class DateFormatException extends RuntimeException {

	private String date;

	public DateFormatException(String date, Throwable cause) {
		
		 super(String.format("%s is not a Date with Standard Data format", date), cause);
		  this.date = date;
	}
	
}
