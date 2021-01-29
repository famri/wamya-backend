package com.excentria_it.wamya.application.port.in;

import java.util.List;

import com.excentria_it.wamya.domain.AutoCompleteLocalitiesDto;

public interface AutoCompleteLocalityUseCase {
	List<AutoCompleteLocalitiesDto> autoCompleteLocality(String input, String countryCode, String locale);
}
