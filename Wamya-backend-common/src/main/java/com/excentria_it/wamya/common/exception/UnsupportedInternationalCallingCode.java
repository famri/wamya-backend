package com.excentria_it.wamya.common.exception;

public class UnsupportedInternationalCallingCode extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7053365377100142788L;

	public UnsupportedInternationalCallingCode(String icc) {
		super(String.format("Unsupported international calling coe %s", icc));
	}
}
