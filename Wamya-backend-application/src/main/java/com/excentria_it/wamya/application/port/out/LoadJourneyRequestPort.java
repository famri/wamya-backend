package com.excentria_it.wamya.application.port.out;

import java.util.Optional;

import com.excentria_it.wamya.domain.CreateJourneyRequestDto;

public interface LoadJourneyRequestPort {
	Optional<CreateJourneyRequestDto> loadJourneyRequestById(Long id);
}
