package com.excentria_it.wamya.adapter.web.exception;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

public class MyBindException extends BindException {

	private static final long serialVersionUID = 1L;

	private String localizedMessage;

	public MyBindException(BindingResult bindingResult, String localizedMessage) {
		super(bindingResult);
		this.localizedMessage = localizedMessage;
	}

	@Override
	public String getLocalizedMessage() {
		return this.localizedMessage;
	}

}
