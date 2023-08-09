package com.excentria_it.wamya.test.data.common;

import java.util.List;

import com.excentria_it.wamya.domain.AutoCompleteLocalityDto;

public class LocalityTestData {
	private static final List<AutoCompleteLocalityDto> autoCompleteLocalitiesDtos = List.of(

			new AutoCompleteLocalityDto(1L, "Cité El Moez 1", "Thyna", "Sfax", "Tunisie"),
			new AutoCompleteLocalityDto(2L, "Cité Thyna El Jadida", "Thyna", "Sfax", "Tunisie"));

	public static List<AutoCompleteLocalityDto> defaultAutoCompleteLocalitiesDtos() {
		return autoCompleteLocalitiesDtos;
	}
}
