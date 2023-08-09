package com.excentria_it.wamya.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class LoadEngineTypesDtoTests {

	@Test
	void testBuilder() {
		LoadEngineTypesDto loadEngineTypesDto = new LoadEngineTypesDto.Builder().withId(1L).withName("EngineType1")
				.withCode("EngineTypeCode").withDescription("EngineTypeDescription").build();
		assertEquals(1L, loadEngineTypesDto.getId());
		assertEquals("EngineType1", loadEngineTypesDto.getName());
		assertEquals("EngineTypeCode", loadEngineTypesDto.getCode());
		assertEquals("EngineTypeDescription", loadEngineTypesDto.getDescription());

	}
}
