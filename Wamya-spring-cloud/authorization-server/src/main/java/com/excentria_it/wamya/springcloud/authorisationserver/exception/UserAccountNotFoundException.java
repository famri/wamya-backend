package com.excentria_it.wamya.springcloud.authorisationserver.exception;

public class UserAccountNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -1676734843705620112L;

	public UserAccountNotFoundException(String message) {

		super(message);

	}

}
