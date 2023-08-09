package com.excentria_it.wamya.domain;

import com.excentria_it.wamya.common.annotation.Generated;

@Generated
public enum ValidationState {
	NOT_VALIDATED(false), PENDING(false), VALIDATED(true);

	private boolean isValidated;

	ValidationState(boolean isValidated) {
		this.isValidated = isValidated;
	}

	public boolean isValidated() {
		return isValidated;
	}

}
