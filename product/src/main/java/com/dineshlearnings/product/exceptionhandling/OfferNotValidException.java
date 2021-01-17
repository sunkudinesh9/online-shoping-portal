package com.dineshlearnings.product.exceptionhandling;

public class OfferNotValidException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OfferNotValidException(String message) {
		super(message);
	}

}
