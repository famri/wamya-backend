package com.excentria_it.wamya.application.port.out;

import java.util.Optional;

import com.excentria_it.wamya.domain.EngineTypeDto;

public interface LoadEngineTypePort {

	Optional<EngineTypeDto> loadEngineTypeById(Long engineTypeId, String locale);
}
