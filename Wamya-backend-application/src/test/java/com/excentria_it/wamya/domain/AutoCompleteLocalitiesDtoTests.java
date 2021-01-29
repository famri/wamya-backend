package com.excentria_it.wamya.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class AutoCompleteLocalitiesDtoTests {

	@Test
	void testBuilder() {
		AutoCompleteLocalitiesDto autoCompleteLocalitiesDto = new AutoCompleteLocalitiesDto.Builder().withId(1L)
				.withName("Cité EL Moez 1").withCountry("Tunisie").withDepartment("Sfax").withDelegation("Thyna")
				.build();
		assertEquals(1L, autoCompleteLocalitiesDto.getId());
		assertEquals("Cité EL Moez 1", autoCompleteLocalitiesDto.getName());
		assertEquals("Sfax", autoCompleteLocalitiesDto.getDepartment());
		assertEquals("Thyna", autoCompleteLocalitiesDto.getDelegation());
		assertEquals("Tunisie", autoCompleteLocalitiesDto.getCountry());
	}
}
