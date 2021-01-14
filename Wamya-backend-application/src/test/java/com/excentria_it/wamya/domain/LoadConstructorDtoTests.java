package com.excentria_it.wamya.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class LoadConstructorDtoTests {
	@Test
	void testBuilder() {
		LoadConstructorsDto loadConstructorDto = new LoadConstructorsDto.Builder().id(1L).name("Constructor1").build();
		assertEquals(1L, loadConstructorDto.getId());
		assertEquals("Constructor1", loadConstructorDto.getName());

	}
}
