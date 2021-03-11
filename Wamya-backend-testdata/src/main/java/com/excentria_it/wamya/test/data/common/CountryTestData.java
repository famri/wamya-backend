package com.excentria_it.wamya.test.data.common;

import java.util.List;

import com.excentria_it.wamya.domain.CountryDto;
import com.excentria_it.wamya.domain.LoadCountriesDto;
import com.excentria_it.wamya.domain.LoadCountriesDto.IccDto;
import com.excentria_it.wamya.domain.LoadCountriesDto.TimeZoneDto;

public class CountryTestData {

	public static CountryDto defaultCountryDto() {
		return new CountryDto(1L, "Tunisie");
	}

	public static List<LoadCountriesDto> defaultLoadCountriesDtos() {
		return List.of(
				new LoadCountriesDto(1L, "Tunisie", "TN", "/content/icons/tunisia-flag-icon-32.png",
						new IccDto(1L, "+216"), List.of(new TimeZoneDto(1L, "Africa/Tunis", "GMT+01:00"))),
				new LoadCountriesDto(1L, "France", "FR", "/content/icons/france-flag-icon-32.png",
						new IccDto(2L, "+33"), List.of(new TimeZoneDto(2L, "Europe/Paris", "GMT+01:00"))));
	}
}
