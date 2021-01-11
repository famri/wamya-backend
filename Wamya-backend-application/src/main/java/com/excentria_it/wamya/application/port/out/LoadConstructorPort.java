package com.excentria_it.wamya.application.port.out;

import java.util.Optional;

import com.excentria_it.wamya.domain.ConstructorDto;

public interface LoadConstructorPort {
	Optional<ConstructorDto> loadConstructorById(Long constructorId);
}
