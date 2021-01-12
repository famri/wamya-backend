package com.excentria_it.wamya.application.service;

import java.util.List;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.LoadConstructorsUseCase;
import com.excentria_it.wamya.application.port.out.LoadConstructorPort;
import com.excentria_it.wamya.application.port.out.LoadModelPort;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.domain.LoadConstructorsDto;
import com.excentria_it.wamya.domain.LoadModelsDto;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class ConstructorService implements LoadConstructorsUseCase {

	private final LoadConstructorPort loadConstructorPort;

	private final LoadModelPort loadModelPort;

	@Override
	public List<LoadConstructorsDto> loadAllConstructors() {
		return loadConstructorPort.loadAllConstructors();
	}

	@Override
	public List<LoadModelsDto> loadConstructorModels(Long constructorId) {

		return loadModelPort.loadByConstructorId(constructorId);

	}

}
