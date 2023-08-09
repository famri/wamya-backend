package com.excentria_it.wamya.application.port.out;

import java.util.Optional;

import com.excentria_it.wamya.domain.CountryDto;

public interface LoadCountryPort {

	Optional<CountryDto> loadCountryByCodeAndLocale(String trim, String locale);
}
