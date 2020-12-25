package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.domain.JourneyRequest;

public interface CreateJourneyRequestPort {

	JourneyRequest createJourneyRequest(JourneyRequest journeyRequest, String username);

}
