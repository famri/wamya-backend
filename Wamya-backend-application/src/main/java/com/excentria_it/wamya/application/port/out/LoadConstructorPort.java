package com.excentria_it.wamya.application.port.out;

import java.util.List;
import java.util.Optional;

import com.excentria_it.wamya.domain.ConstructorDto;
import com.excentria_it.wamya.domain.LoadConstructorsDto;

public interface LoadConstructorPort {
	Optional<ConstructorDto> loadConstructorById(Long constructorId);

	List<LoadConstructorsDto> loadAllConstructors();
}
