package com.excentria_it.wamya.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.domain.LoadCountriesDto.IccDto;

public class IccDtoTests {
	@Test
	void testBuilder() {
		IccDto iccDto = new IccDto.Builder().withId(1L).withValue("+216").build();
		assertEquals(1L, iccDto.getId());
		assertEquals("+216", iccDto.getValue());

	}
}
