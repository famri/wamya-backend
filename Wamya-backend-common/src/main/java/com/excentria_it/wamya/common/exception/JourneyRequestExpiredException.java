package com.excentria_it.wamya.common.exception;

public class JourneyRequestExpiredException extends RuntimeException {

	private static final long serialVersionUID = -5143965169703559152L;

	public JourneyRequestExpiredException(String message) {
		super(message);
	}

}