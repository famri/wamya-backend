package com.excentria_it.wamya.application.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.AutoCompletePlaceForTransporterUseCase;
import com.excentria_it.wamya.application.port.out.LoadCountryPort;
import com.excentria_it.wamya.application.port.out.SearchDepartmentPort;
import com.excentria_it.wamya.application.utils.AutoCompletePlaceMapper;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.exception.CountryNotFoundException;
import com.excentria_it.wamya.domain.AutoCompleteDepartmentDto;
import com.excentria_it.wamya.domain.AutoCompletePlaceDto;
import com.excentria_it.wamya.domain.CountryDto;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class AutoCompletePlaceForTransporterService implements AutoCompletePlaceForTransporterUseCase {

	private final SearchDepartmentPort searchDepartmentPort;

	private final LoadCountryPort loadCountryPort;

	@Override
	public List<AutoCompletePlaceDto> autoCompleteDepartment(String input, String countryCode, Integer limit,
			String locale) {

		Optional<CountryDto> countryDtoOptional = loadCountryPort.loadCountryByCodeAndLocale(countryCode.trim(),
				locale);
		if (countryDtoOptional.isEmpty()) {
			throw new CountryNotFoundException(String.format("County not found by code: %s", countryCode));
		}
		CountryDto countryDto = countryDtoOptional.get();

		List<AutoCompleteDepartmentDto> departments = searchDepartmentPort.searchDepartmentByName(input.trim(),
				countryDto.getId(), limit, locale);

		return departments.stream().map(dep -> AutoCompletePlaceMapper.mapDepartmentToPlace(dep))
				.collect(Collectors.toList());

	}

}
