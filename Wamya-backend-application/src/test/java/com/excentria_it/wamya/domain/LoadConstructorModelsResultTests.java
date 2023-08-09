package com.excentria_it.wamya.domain;

import static com.excentria_it.wamya.test.data.common.ModelTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

public class LoadConstructorModelsResultTests {

	@Test
	void testBuilder() {
		List<LoadModelsDto> loadModelsDtos = defaultLoadModelsDtos();
		LoadConstructorModelsResult loadConstructorModelsResult = new LoadConstructorModelsResult.Builder()
				.withTotalElements(1).withContent(loadModelsDtos).build();

		assertEquals(loadModelsDtos, loadConstructorModelsResult.getContent());
		assertEquals(1, loadConstructorModelsResult.getTotalElements());
	}
}
