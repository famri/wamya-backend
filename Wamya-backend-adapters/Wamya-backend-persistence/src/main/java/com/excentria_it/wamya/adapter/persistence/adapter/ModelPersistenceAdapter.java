package com.excentria_it.wamya.adapter.persistence.adapter;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.excentria_it.wamya.adapter.persistence.repository.ModelRepository;
import com.excentria_it.wamya.application.port.out.LoadModelPort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.domain.LoadModelsDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class ModelPersistenceAdapter implements LoadModelPort {

	private final ModelRepository modelRepository;

	@Override
	public List<LoadModelsDto> loadByConstructorId(Long constructorId) {

		return modelRepository.findByConstructor_Id(constructorId, Sort.by(new Order(Direction.ASC, "name")));
	}

}
