package com.excentria_it.wamya.springcloud.authorisationserver.exception;

public class UserAccountAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 3195446084366173032L;

	public UserAccountAlreadyExistsException(String message) {

		super(message);

	}

}
