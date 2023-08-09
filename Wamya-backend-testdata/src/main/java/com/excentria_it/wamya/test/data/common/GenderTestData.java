package com.excentria_it.wamya.test.data.common;

import java.util.List;

import com.excentria_it.wamya.domain.LoadGendersDto;

public class GenderTestData {

	private static final List<LoadGendersDto> loadGendersDtos = List.of(new LoadGendersDto(1L, "MAN"),
			new LoadGendersDto(2L, "WOMAN"));

	public static LoadGendersDto defaultLoadGendersDto() {
		return loadGendersDtos.get(0);
	}

	public static List<LoadGendersDto> defaultLoadGendersDtos() {
		return loadGendersDtos;
	}
}
