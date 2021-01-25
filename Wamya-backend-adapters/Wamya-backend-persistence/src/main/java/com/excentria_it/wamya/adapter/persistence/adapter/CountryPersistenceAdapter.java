package com.excentria_it.wamya.adapter.persistence.adapter;

import java.util.Optional;

import com.excentria_it.wamya.adapter.persistence.repository.CountryRepository;
import com.excentria_it.wamya.application.port.out.LoadCountryPort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.domain.CountryDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class CountryPersistenceAdapter implements LoadCountryPort {

	private final CountryRepository countryRepository;

	@Override
	public Optional<CountryDto> loadCountryByCodeAndLocale(String code, String locale) {
		return countryRepository.findByCodeAndLocale(code, locale);
	}

}
