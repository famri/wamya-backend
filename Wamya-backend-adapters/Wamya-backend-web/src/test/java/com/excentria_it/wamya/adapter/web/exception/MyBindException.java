package com.excentria_it.wamya.adapter.web.exception;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

public class MyBindException extends BindException {

	private static final long serialVersionUID = 1L;

	

	public MyBindException(BindingResult bindingResult) {
		super(bindingResult);

	}

}
