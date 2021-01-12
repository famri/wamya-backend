package com.excentria_it.wamya.application.service;

import java.util.List;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.LoadEngineTypesUseCase;
import com.excentria_it.wamya.application.port.out.LoadEngineTypePort;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.domain.LoadEngineTypesDto;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class EngineTypeService implements LoadEngineTypesUseCase {
	private final LoadEngineTypePort loadEngineTypePort;

	@Override
	public List<LoadEngineTypesDto> loadAllEngineTypes(String locale) {

		return loadEngineTypePort.loadAllEngineTypes(locale);
	}

}
