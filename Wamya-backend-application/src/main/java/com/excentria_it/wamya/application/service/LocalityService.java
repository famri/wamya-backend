package com.excentria_it.wamya.application.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.AutoCompleteLocalityUseCase;
import com.excentria_it.wamya.application.port.out.LoadCountryPort;
import com.excentria_it.wamya.application.port.out.SearchLocalityPort;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.exception.CountryNotFoundException;
import com.excentria_it.wamya.domain.AutoCompleteLocalitiesDto;
import com.excentria_it.wamya.domain.CountryDto;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class LocalityService implements AutoCompleteLocalityUseCase {

	private final SearchLocalityPort searchLocalityPort;

	private final LoadCountryPort loadCountryPort;

	@Override
	public List<AutoCompleteLocalitiesDto> autoCompleteLocality(String input, String countryCode, String locale) {
		Optional<CountryDto> countryDtoOptional = loadCountryPort.loadCountryByCodeAndLocale(countryCode.trim(),
				locale);
		if (countryDtoOptional.isEmpty()) {
			throw new CountryNotFoundException(String.format("County not found by code: %s", countryCode));
		}
		CountryDto countryDto = countryDtoOptional.get();

		return searchLocalityPort.searchLocality(input.trim(), countryDto.getId(), locale);
	}

}
