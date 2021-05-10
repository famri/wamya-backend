package com.excentria_it.wamya.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class LoadGendersDtoTests {

	@Test
	void testBuilder() {
		LoadGendersDto loadGendersDto = new LoadGendersDto.Builder().withId(1L).withName("MAN").build();
		assertEquals(1L, loadGendersDto.getId());
		assertEquals("MAN", loadGendersDto.getName());

	}
}
