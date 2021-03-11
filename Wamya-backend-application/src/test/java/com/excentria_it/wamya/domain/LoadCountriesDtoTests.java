package com.excentria_it.wamya.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.domain.LoadCountriesDto.IccDto;
import com.excentria_it.wamya.domain.LoadCountriesDto.TimeZoneDto;

public class LoadCountriesDtoTests {
	@Test
	void testBuilder() {

		List<TimeZoneDto> timeZones = List.of(new TimeZoneDto(1L, "Africa/Tunis", "GMT+01:00"));
		IccDto icc = new IccDto(1L, "+216");
		LoadCountriesDto loadCountriesDto = new LoadCountriesDto.Builder().withId(1L).withName("Tunisie").withCode("TN")
				.withFlagPath("/content/icons/tunisia-flag-icon-32.png").withIcc(icc).withTimeZones(timeZones).build();

		assertEquals(1L, loadCountriesDto.getId());
		assertEquals("Tunisie", loadCountriesDto.getName());
		assertEquals("TN", loadCountriesDto.getCode());
		assertEquals(icc, loadCountriesDto.getIcc());

		assertEquals(timeZones, loadCountriesDto.getTimeZones());
	}
}
