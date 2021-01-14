package com.excentria_it.wamya.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class LoadEngineTypesDtoTests {

	@Test
	void testBuilder() {
		LoadEngineTypesDto loadEngineTypesDto = new LoadEngineTypesDto.Builder().id(1L).name("EngineType1")
				.code("EngineTypeCode").description("EngineTypeDescription").build();
		assertEquals(1L, loadEngineTypesDto.getId());
		assertEquals("EngineType1", loadEngineTypesDto.getName());
		assertEquals("EngineTypeCode", loadEngineTypesDto.getCode());
		assertEquals("EngineTypeDescription", loadEngineTypesDto.getDescription());

	}
}
