package com.excentria_it.wamya.common.exception;

public class LoginOrPasswordNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1522571352115107648L;

	public LoginOrPasswordNotFoundException(String message) {
		super(message);
	}

}
