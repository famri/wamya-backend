package com.codisiac.wamya.common;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;

public class SelfValidatingTest {

	@Test
	void whenNotValidInstance_thenThrowConstraintViolationException() {
		ConcreteClassToValidate cctv = new ConcreteClassToValidate(null);
		assertThrows(ConstraintViolationException.class, () -> cctv.validateSelf());
	}

	@Test
	void whenValidInstance_thenReturnEmptyConstraintViolationCollection() {
		ConcreteClassToValidate cctv = new ConcreteClassToValidate("SOME_NOT_NULL_STRING");
		assertThat(cctv.validateSelf()).isEmpty();
	}
}
