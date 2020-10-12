package com.excentria_it.wamya.common.exception;

public class UserAccountNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1908025927024214919L;

	public UserAccountNotFoundException(String message) {
		super(message);
	}

}
