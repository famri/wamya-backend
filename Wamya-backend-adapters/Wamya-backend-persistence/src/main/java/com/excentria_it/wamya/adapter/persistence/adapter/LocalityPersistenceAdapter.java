package com.excentria_it.wamya.adapter.persistence.adapter;

import java.util.List;

import com.excentria_it.wamya.adapter.persistence.repository.LocalityRepository;
import com.excentria_it.wamya.application.port.out.SearchLocalityPort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.domain.AutoCompleteLocalitiesDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class LocalityPersistenceAdapter implements SearchLocalityPort {

	private final LocalityRepository localityRepository;

	@Override
	public List<AutoCompleteLocalitiesDto> searchLocality(String input, Long countryId, String locale) {

		return localityRepository.findByCountry_IdAndNameLikeIgnoringCase(countryId, input, locale);
	}

}
