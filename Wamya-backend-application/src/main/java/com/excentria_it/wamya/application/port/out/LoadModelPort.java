package com.excentria_it.wamya.application.port.out;

import java.util.List;

import com.excentria_it.wamya.domain.LoadModelsDto;

public interface LoadModelPort {

	List<LoadModelsDto> loadByConstructorId(Long constructorId);

}
