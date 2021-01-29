package com.excentria_it.wamya.application.port.out;

import java.util.List;

import com.excentria_it.wamya.domain.AutoCompleteLocalitiesDto;

public interface SearchLocalityPort {

	List<AutoCompleteLocalitiesDto> searchLocality(String input, Long countryId, String locale);

}
