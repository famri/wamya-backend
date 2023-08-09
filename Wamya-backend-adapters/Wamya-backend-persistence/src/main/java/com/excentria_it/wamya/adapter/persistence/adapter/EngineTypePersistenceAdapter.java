package com.excentria_it.wamya.adapter.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.excentria_it.wamya.adapter.persistence.repository.EngineTypeRepository;
import com.excentria_it.wamya.application.port.out.LoadEngineTypePort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.domain.EngineTypeDto;
import com.excentria_it.wamya.domain.LoadEngineTypesDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class EngineTypePersistenceAdapter implements LoadEngineTypePort {

	private final EngineTypeRepository engineTypeRepository;

	@Override
	public Optional<EngineTypeDto> loadEngineTypeById(Long engineTypeId, String locale) {
		return engineTypeRepository.findByIdAndLocale(engineTypeId, locale);
	}

	@Override
	public List<LoadEngineTypesDto> loadAllEngineTypes(String locale) {

		return engineTypeRepository.findAllByLocale(locale, Sort.by(new Order(Direction.ASC, "rank")));
	}

}
