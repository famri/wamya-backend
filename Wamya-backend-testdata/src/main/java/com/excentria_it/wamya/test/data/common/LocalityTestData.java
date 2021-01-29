package com.excentria_it.wamya.test.data.common;

import java.util.List;

import com.excentria_it.wamya.domain.AutoCompleteLocalitiesDto;

public class LocalityTestData {
	private static final List<AutoCompleteLocalitiesDto> autoCompleteLocalitiesDtos = List.of(

			new AutoCompleteLocalitiesDto(1L, "Cité El Moez 1", "Thyna", "Sfax", "Tunisie"),
			new AutoCompleteLocalitiesDto(1L, "Cité Thyna El Jadida", "Thyna", "Sfax", "Tunisie"));

	public static List<AutoCompleteLocalitiesDto> defaultAutoCompleteLocalitiesDtos() {
		return autoCompleteLocalitiesDtos;
	}
}
