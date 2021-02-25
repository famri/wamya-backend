package com.excentria_it.wamya.test.data.common;

import java.util.List;

import com.excentria_it.wamya.domain.AutoCompleteDelegationDto;

public class DelegationTestData {
	private static final List<AutoCompleteDelegationDto> autoCompleteDelegationDtos = List.of(

			new AutoCompleteDelegationDto(1L, "Thyna", "Sfax", "Tunisie"),
			new AutoCompleteDelegationDto(2L, "Chebba", "Mahdia", "Tunisie"));

	public static List<AutoCompleteDelegationDto> defaultAutoCompleteDelegationDtos() {
		return autoCompleteDelegationDtos;
	}
}
