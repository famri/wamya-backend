package com.excentria_it.wamya.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class LoadLocalesDtoTests {

	@Test
	void testBuilder() {
		LoadLocalesDto loadLocalesDto = new LoadLocalesDto.Builder().withId(1L).withName("Français")
				.withLanguageCode("fr").withCountryCode("FR").build();
		assertEquals(1L, loadLocalesDto.getId());
		assertEquals("Français", loadLocalesDto.getName());
		assertEquals("fr", loadLocalesDto.getLanguageCode());
		assertEquals("FR", loadLocalesDto.getCountryCode());
	}
}
