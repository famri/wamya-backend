package com.excentria_it.wamya.application.port.in;

import com.excentria_it.wamya.application.port.in.CreateJourneyRequestUseCase.CreateJourneyRequestCommand;

public interface UpdateJourneyRequestUseCase {

	void updateJourneyRequest(Long journeyRequestId, CreateJourneyRequestCommand command, String username, String locale);

}
