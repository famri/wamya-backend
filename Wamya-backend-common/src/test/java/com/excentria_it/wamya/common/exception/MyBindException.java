package com.excentria_it.wamya.common.exception;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

public class MyBindException extends BindException {

	private static final long serialVersionUID = 1L;

	

	public MyBindException(BindingResult bindingResult) {
		super(bindingResult);

	}

}
