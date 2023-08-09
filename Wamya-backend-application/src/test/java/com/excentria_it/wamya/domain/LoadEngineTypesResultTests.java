package com.excentria_it.wamya.domain;

import static com.excentria_it.wamya.test.data.common.EngineTypeTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

public class LoadEngineTypesResultTests {
	@Test
	void testBuilder() {
		List<LoadEngineTypesDto> loadEngineTypesDtos = defaultLoadEngineTypesDtos();
		LoadEngineTypesResult loadEngineTypesResult = new LoadEngineTypesResult.Builder().withTotalElements(1)
				.withContent(loadEngineTypesDtos).build();

		assertEquals(loadEngineTypesDtos, loadEngineTypesResult.getContent());
		assertEquals(1, loadEngineTypesResult.getTotalElements());
	}
}
