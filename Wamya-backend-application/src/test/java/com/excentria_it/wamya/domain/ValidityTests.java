package com.excentria_it.wamya.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

import org.junit.jupiter.api.Test;

public class ValidityTests {

	@Test
	void testValidity() {
		for (Validity validity : Validity.values()) {

			Instant startInstant = Instant.now();
			Instant expiry = validity.calculateExpiry(startInstant);
			assertEquals(startInstant.plusMillis(validity.getValidityDelay()), expiry);
		}
	}

	@Test
	void testValidityCalculateExpiryWithNullStartInstant() {
		assertNull(Validity.H3.calculateExpiry(null));
	}

}