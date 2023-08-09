package com.excentria_it.wamya.domain;

import static com.excentria_it.wamya.test.data.common.ConstructorTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

public class LoadConstructorsResultTest {
	@Test
	void testBuilder() {
		List<LoadConstructorsDto> loadConstructorsDto = defaultLoadConstructorsDtos();
		LoadConstructorsResult loadConstructorsResult = new LoadConstructorsResult.Builder().withTotalElements(1)
				.withContent(loadConstructorsDto).build();

		assertEquals(loadConstructorsDto, loadConstructorsResult.getContent());
		assertEquals(1, loadConstructorsResult.getTotalElements());
	}
}
