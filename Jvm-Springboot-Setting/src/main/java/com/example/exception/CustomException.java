package com.example.exception;

import lombok.Getter;
@Getter
public class CustomException extends Exception {

	private static final long serialVersionUID = 8576377462856805128L;
	
	private String errorDesc;
	
	public CustomException() {
		super();
	}
	
	public CustomException(String errorDesc) {
		super(errorDesc);
		this.errorDesc = errorDesc;
	}
	
	

}
