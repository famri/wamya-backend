package com.excentria_it.wamya.domain;

import java.time.Instant;

public enum Validity {
	H3(10800000L);

	private long milliseconds;

	private Validity(long milliseconds) {
		this.milliseconds = milliseconds;
	}

	public Instant calculateExpiry(Instant startInstant) {
		if (startInstant == null)
			return null;
		return startInstant.plusMillis(this.milliseconds);

	}

	public Long getValidityDelay() {
		return this.milliseconds;
	}

}
