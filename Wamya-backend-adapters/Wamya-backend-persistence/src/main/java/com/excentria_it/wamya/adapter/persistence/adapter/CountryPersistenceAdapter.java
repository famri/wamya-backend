package com.excentria_it.wamya.adapter.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.excentria_it.wamya.adapter.persistence.entity.CountryJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.CountryMapper;
import com.excentria_it.wamya.adapter.persistence.repository.CountryRepository;
import com.excentria_it.wamya.application.port.out.LoadCountriesPort;
import com.excentria_it.wamya.application.port.out.LoadCountryPort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.domain.CountryDto;
import com.excentria_it.wamya.domain.LoadCountriesDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class CountryPersistenceAdapter implements LoadCountryPort, LoadCountriesPort {

	private final CountryRepository countryRepository;
	private final CountryMapper countryMapper;

	@Override
	public Optional<CountryDto> loadCountryByCodeAndLocale(String code, String locale) {
		return countryRepository.findByCodeAndLocale(code, locale);
	}

	@Override
	public List<LoadCountriesDto> loadAllCountries(String locale) {

		List<CountryJpaEntity> countries = countryRepository.findAllByLocale(locale);
		return countries.stream().map(c -> countryMapper.mapToDomainEntity(c, locale)).collect(Collectors.toList());
	}

}
