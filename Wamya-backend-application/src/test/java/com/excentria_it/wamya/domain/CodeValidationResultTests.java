package com.excentria_it.wamya.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CodeValidationResultTests {
	@Test
	void testBuilder() {
		CodeValidationResult codeValidationResult = new CodeValidationResult.Builder().withValid(true).build();
		assertEquals(true, codeValidationResult.isValid());
	}
}
