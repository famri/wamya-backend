package com.excentria_it.wamya.application.port.out;

import java.util.List;

import com.excentria_it.wamya.domain.LoadCountriesDto;

public interface LoadCountriesPort {

	public List<LoadCountriesDto> loadAllCountries(String locale);

}
