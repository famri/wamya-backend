package com.excentria_it.wamya.domain;

import static com.excentria_it.wamya.test.data.common.LocalityTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

public class AutoCompleteLocalitiesResultTests {
	@Test
	void testBuilder() {
		List<AutoCompleteLocalitiesDto> autoCompleteLocalitiesDtos = defaultAutoCompleteLocalitiesDtos();
		AutoCompleteLocalitiesResult autoCompleteLocalitiesResult = new AutoCompleteLocalitiesResult.Builder()
				.withTotalElements(2).withContent(autoCompleteLocalitiesDtos).build();

		assertEquals(autoCompleteLocalitiesDtos, autoCompleteLocalitiesResult.getContent());
		assertEquals(2, autoCompleteLocalitiesResult.getTotalElements());
	}
}
