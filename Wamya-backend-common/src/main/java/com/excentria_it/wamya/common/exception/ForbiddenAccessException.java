package com.excentria_it.wamya.common.exception;

public class ForbiddenAccessException extends RuntimeException {

	private static final long serialVersionUID = 4022605511859488334L;

	public ForbiddenAccessException(String message) {
		super(message);
	}

}