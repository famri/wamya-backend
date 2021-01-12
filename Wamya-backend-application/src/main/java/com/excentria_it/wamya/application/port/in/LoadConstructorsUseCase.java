package com.excentria_it.wamya.application.port.in;

import java.util.List;

import com.excentria_it.wamya.domain.LoadConstructorsDto;
import com.excentria_it.wamya.domain.LoadModelsDto;

public interface LoadConstructorsUseCase {

	List<LoadConstructorsDto> loadAllConstructors();

	List<LoadModelsDto> loadConstructorModels(Long constructorId);

}
