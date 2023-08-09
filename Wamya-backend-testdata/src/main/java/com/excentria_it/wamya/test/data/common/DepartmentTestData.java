package com.excentria_it.wamya.test.data.common;

import java.util.List;

import com.excentria_it.wamya.domain.AutoCompleteDepartmentDto;

public class DepartmentTestData {
	private static final List<AutoCompleteDepartmentDto> autoCompleteDepartmentsDto = List.of(

			new AutoCompleteDepartmentDto(1L, "Ben arous", "Tunisie"),
			new AutoCompleteDepartmentDto(2L, "Béja", "Tunisie"));

	public static List<AutoCompleteDepartmentDto> defaultAutoCompleteDepartmentsDtos() {
		return autoCompleteDepartmentsDto;
	}
}
