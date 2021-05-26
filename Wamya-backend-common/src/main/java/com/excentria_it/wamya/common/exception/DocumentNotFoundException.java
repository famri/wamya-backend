package com.excentria_it.wamya.common.exception;

public class DocumentNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 5283432585218553565L;

	public DocumentNotFoundException(String message) {
		super(message);
	}

}