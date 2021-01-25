package com.excentria_it.wamya.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class AutoCompleteDepartmentDtoTests {

	@Test
	void testBuilder() {
		AutoCompleteDepartmentsDto autoCompleteDepartmentsDto = new AutoCompleteDepartmentsDto.Builder().withId(1L)
				.withName("Ben arous").withCountry("Tunisie").build();
		assertEquals(1L, autoCompleteDepartmentsDto.getId());
		assertEquals("Ben arous", autoCompleteDepartmentsDto.getName());
		assertEquals("Tunisie", autoCompleteDepartmentsDto.getCountry());
	}
}
