package com.excentria_it.wamya.domain;

import static com.excentria_it.wamya.test.data.common.CountryTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

public class LoadCountriesResultTests {
	@Test
	void testBuilder() {
		List<LoadCountriesDto> loadCountriesDtos = defaultLoadCountriesDtos();
		LoadCountriesResult loadCountriesResult = new LoadCountriesResult.Builder().withTotalElements(2)
				.withContent(loadCountriesDtos).build();

		assertEquals(loadCountriesDtos, loadCountriesResult.getContent());
		assertEquals(2, loadCountriesResult.getTotalElements());
	}
}
