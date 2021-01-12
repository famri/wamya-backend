package com.excentria_it.wamya.application.port.in;

import java.util.List;

import com.excentria_it.wamya.domain.LoadEngineTypesDto;

public interface LoadEngineTypesUseCase {

	List<LoadEngineTypesDto> loadAllEngineTypes(String locale);

}
