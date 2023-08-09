package com.excentria_it.wamya.application.port.out;

import java.util.List;
import java.util.Optional;

import com.excentria_it.wamya.domain.EngineTypeDto;
import com.excentria_it.wamya.domain.LoadEngineTypesDto;

public interface LoadEngineTypePort {

	Optional<EngineTypeDto> loadEngineTypeById(Long engineTypeId, String locale);

	List<LoadEngineTypesDto> loadAllEngineTypes(String locale);
}
