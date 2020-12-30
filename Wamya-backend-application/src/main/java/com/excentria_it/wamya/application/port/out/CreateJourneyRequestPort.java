package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.domain.CreateJourneyRequestDto;

public interface CreateJourneyRequestPort {

	CreateJourneyRequestDto createJourneyRequest(CreateJourneyRequestDto journeyRequest, String username);

}
