package com.excentria_it.wamya.domain;

import static com.excentria_it.wamya.test.data.common.GenderTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

public class LoadGendersResultTests {
	@Test
	void testBuilder() {
		List<LoadGendersDto> loadGendersDtos = defaultLoadGendersDtos();
		LoadGendersResult loadGendersResult = new LoadGendersResult.Builder().withTotalElements(1)
				.withContent(loadGendersDtos).build();

		assertEquals(loadGendersDtos, loadGendersResult.getContent());
		assertEquals(1, loadGendersResult.getTotalElements());
	}
}
