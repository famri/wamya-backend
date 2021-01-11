package com.excentria_it.wamya.adapter.persistence.adapter;

import java.util.Optional;

import com.excentria_it.wamya.adapter.persistence.repository.ConstructorRepository;
import com.excentria_it.wamya.application.port.out.LoadConstructorPort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.domain.ConstructorDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class ConstructorPersistenceAdapter implements LoadConstructorPort {
	private final ConstructorRepository constructorRepository;

	@Override
	public Optional<ConstructorDto> loadConstructorById(Long constructorId) {
	
		return constructorRepository.findConstructorById(constructorId);
	}

}
