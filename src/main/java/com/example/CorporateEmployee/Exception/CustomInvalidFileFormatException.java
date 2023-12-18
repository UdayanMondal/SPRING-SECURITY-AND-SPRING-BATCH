package com.example.CorporateEmployee.Exception;

public class CustomInvalidFileFormatException  extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public CustomInvalidFileFormatException(String message ) {
		
		 super(message);
		  
	}
}
