package com.excentria_it.wamya.application.port.in;

import com.excentria_it.wamya.domain.UpdatableJourneyRequest;

public interface LoadUpdatableJourneyRequestUseCase {

	UpdatableJourneyRequest loadJourneyRequest(Long journeyRequestId);

}
