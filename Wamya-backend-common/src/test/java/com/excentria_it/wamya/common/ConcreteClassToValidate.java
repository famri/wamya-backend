package com.excentria_it.wamya.common;

import javax.validation.constraints.NotNull;

import com.excentria_it.wamya.common.SelfValidating;

public class ConcreteClassToValidate extends SelfValidating<ConcreteClassToValidate> {
	@NotNull
	private String someField = null;

	public ConcreteClassToValidate(String someField) {
		super();
		this.someField = someField;
	}

}
