package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.domain.JourneyRequestInputOutput;

public interface CreateJourneyRequestPort {

	JourneyRequestInputOutput createJourneyRequest(JourneyRequestInputOutput journeyRequest, String username,
			String locale);

}
