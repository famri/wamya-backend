package com.excentria_it.wamya.adapter.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.excentria_it.wamya.adapter.persistence.repository.ConstructorRepository;
import com.excentria_it.wamya.application.port.out.LoadConstructorPort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.domain.ConstructorDto;
import com.excentria_it.wamya.domain.LoadConstructorsDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class ConstructorPersistenceAdapter implements LoadConstructorPort {
	private final ConstructorRepository constructorRepository;

	@Override
	public Optional<ConstructorDto> loadConstructorById(Long constructorId) {

		return constructorRepository.findConstructorById(constructorId);
	}

	@Override
	public List<LoadConstructorsDto> loadAllConstructors() {
		
		return constructorRepository.findAllBy(Sort.by(new Order(Direction.ASC, "name")));
	}

}
