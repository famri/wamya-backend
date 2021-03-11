package com.excentria_it.wamya.application.port.in;

import java.util.List;

import com.excentria_it.wamya.domain.LoadCountriesDto;

public interface LoadCountriesUseCase {

	List<LoadCountriesDto> loadAllCountries(String locale);

}
