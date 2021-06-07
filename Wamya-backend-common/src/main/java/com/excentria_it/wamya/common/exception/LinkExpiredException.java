package com.excentria_it.wamya.common.exception;

public class LinkExpiredException extends RuntimeException {

	private static final long serialVersionUID = -7829893270884360570L;

	public LinkExpiredException(String message) {
		super(message);
	}

}
