package com.dineshlearnings.product.exceptionhandling;

public class CurrencynotValidException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CurrencynotValidException(String message) {
		super(message);
	}

}
