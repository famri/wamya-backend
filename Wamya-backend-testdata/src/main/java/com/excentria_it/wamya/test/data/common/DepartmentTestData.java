package com.excentria_it.wamya.test.data.common;

import java.util.List;

import com.excentria_it.wamya.domain.AutoCompleteDepartmentsDto;

public class DepartmentTestData {
	private static final List<AutoCompleteDepartmentsDto> autoCompleteDepartmentsDto = List.of(

			new AutoCompleteDepartmentsDto(1L, "Ben arous", "Tunisie"),
			new AutoCompleteDepartmentsDto(1L, "Béja", "Tunisie"));

	public static List<AutoCompleteDepartmentsDto> defaultAutoCompleteDepartmentsDtos() {
		return autoCompleteDepartmentsDto;
	}
}
