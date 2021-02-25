package com.excentria_it.wamya.application.utils;

import com.excentria_it.wamya.domain.AutoCompleteDelegationDto;
import com.excentria_it.wamya.domain.AutoCompleteDepartmentDto;
import com.excentria_it.wamya.domain.AutoCompleteLocalityDto;
import com.excentria_it.wamya.domain.AutoCompletePlaceDto;
import com.excentria_it.wamya.domain.PlaceType;

public class AutoCompletePlaceMapper {

	private AutoCompletePlaceMapper() {

	}

	public static AutoCompletePlaceDto mapLocalityToPlace(AutoCompleteLocalityDto autoCompleteLocalityDto) {

		return new AutoCompletePlaceDto(autoCompleteLocalityDto.getId(), PlaceType.LOCALITY,
				autoCompleteLocalityDto.getName(), autoCompleteLocalityDto.getDelegation(),
				autoCompleteLocalityDto.getDepartment(), autoCompleteLocalityDto.getCountry());

	}

	public static AutoCompletePlaceDto mapDelegationToPlace(AutoCompleteDelegationDto autoCompleteDelegationDto) {

		return new AutoCompletePlaceDto(autoCompleteDelegationDto.getId(), PlaceType.DELEGATION,
				autoCompleteDelegationDto.getName(), null, autoCompleteDelegationDto.getDepartment(),
				autoCompleteDelegationDto.getCountry());

	}

	public static AutoCompletePlaceDto mapDepartmentToPlace(AutoCompleteDepartmentDto autoCompleteDepartmentDto) {

		return new AutoCompletePlaceDto(autoCompleteDepartmentDto.getId(), PlaceType.DEPARTMENT,
				autoCompleteDepartmentDto.getName(), null, null, autoCompleteDepartmentDto.getCountry());

	}
}
