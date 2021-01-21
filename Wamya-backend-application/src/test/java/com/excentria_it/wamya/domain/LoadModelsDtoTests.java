package com.excentria_it.wamya.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class LoadModelsDtoTests {

	@Test
	void testBuilder() {
		LoadModelsDto loadModelsDto = new LoadModelsDto.Builder().withId(1L).withName("Model1").withLength(4.25)
				.withWidth(1.85).withHeight(1.8).build();

		assertEquals(1L, loadModelsDto.getId());
		assertEquals("Model1", loadModelsDto.getName());
		assertEquals(4.25, loadModelsDto.getLength());
		assertEquals(1.85, loadModelsDto.getWidth());
		assertEquals(1.8, loadModelsDto.getHeight());
	}
}
