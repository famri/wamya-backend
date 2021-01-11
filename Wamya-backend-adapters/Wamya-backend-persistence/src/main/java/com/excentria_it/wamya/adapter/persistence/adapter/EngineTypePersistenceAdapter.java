package com.excentria_it.wamya.adapter.persistence.adapter;

import java.util.Optional;

import com.excentria_it.wamya.adapter.persistence.repository.EngineTypeRepository;
import com.excentria_it.wamya.application.port.out.LoadEngineTypePort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.domain.EngineTypeDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class EngineTypePersistenceAdapter implements LoadEngineTypePort {

	private final EngineTypeRepository engineTypeRepository;

	@Override
	public Optional<EngineTypeDto> loadEngineTypeById(Long engineTypeId, String locale) {
		return engineTypeRepository.findByIdAndLocale(engineTypeId, locale);
	}

}
