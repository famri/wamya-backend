package com.excentria_it.wamya.common;

import javax.validation.constraints.NotNull;

public class ConcreteClassToValidate extends SelfValidating<ConcreteClassToValidate> {
	@NotNull
	private String someField = null;

	public ConcreteClassToValidate(String someField) {
		super();
		this.someField = someField;
	}

}
