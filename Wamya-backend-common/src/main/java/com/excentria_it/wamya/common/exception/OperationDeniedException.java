package com.excentria_it.wamya.common.exception;

public class OperationDeniedException extends RuntimeException {

	private static final long serialVersionUID = 8155032105031059848L;

	public OperationDeniedException(String message) {
		super(message);
	}

}
