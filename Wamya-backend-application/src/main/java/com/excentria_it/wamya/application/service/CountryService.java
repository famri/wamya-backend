package com.excentria_it.wamya.application.service;

import java.util.List;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.LoadCountriesUseCase;
import com.excentria_it.wamya.application.port.out.LoadCountriesPort;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.domain.LoadCountriesDto;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class CountryService implements LoadCountriesUseCase {
	private final LoadCountriesPort loadCountriesPort;

	@Override
	public List<LoadCountriesDto> loadAllCountries(String locale) {
		return loadCountriesPort.loadAllCountries(locale);
	}

}
