package com.excentria_it.wamya.common.exception;

public class DepartmentNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -5889978313994065660L;

	public DepartmentNotFoundException(String message) {
		super(message);
	}

}