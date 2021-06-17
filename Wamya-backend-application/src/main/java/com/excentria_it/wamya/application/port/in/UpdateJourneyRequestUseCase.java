package com.excentria_it.wamya.application.port.in;

import javax.validation.Valid;

import com.excentria_it.wamya.application.port.in.CreateJourneyRequestUseCase.CreateJourneyRequestCommand;

public interface UpdateJourneyRequestUseCase {

	void updateJourneyRequest(Long journeyRequestId, @Valid CreateJourneyRequestCommand command, String name);

}
