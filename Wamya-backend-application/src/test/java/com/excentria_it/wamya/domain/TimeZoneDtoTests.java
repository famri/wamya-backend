package com.excentria_it.wamya.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TimeZoneDtoTests {
	@Test
	void testBuilder() {
		LoadCountriesDto.TimeZoneDto timeZoneDto = new LoadCountriesDto.TimeZoneDto.Builder().withId(1L)
				.withName("Africa/Tunis").withGmtOffset("GMT+01:00").build();
		assertEquals(1L, timeZoneDto.getId());
		assertEquals("Africa/Tunis", timeZoneDto.getName());
		assertEquals("GMT+01:00", timeZoneDto.getGmtOffset());
	}
}
