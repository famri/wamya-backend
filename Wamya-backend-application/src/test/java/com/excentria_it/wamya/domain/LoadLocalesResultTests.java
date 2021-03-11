package com.excentria_it.wamya.domain;

import static com.excentria_it.wamya.test.data.common.LocaleTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

public class LoadLocalesResultTests {
	@Test
	void testBuilder() {
		List<LoadLocalesDto> loadLocalesDtos = defaultLoadLocalesDtos();
		LoadLocalesResult loadLocalesResult = new LoadLocalesResult.Builder().withTotalElements(2)
				.withContent(loadLocalesDtos).build();

		assertEquals(loadLocalesDtos, loadLocalesResult.getContent());
		assertEquals(2, loadLocalesResult.getTotalElements());
	}
}
