package com.excentria_it.wamya.common.exception;

public class ConstructorNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -8517721191238321550L;

	public ConstructorNotFoundException(String message) {
		super(message);
	}

}