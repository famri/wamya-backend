package com.excentria_it.wamya.application.port.out;

import java.util.Set;

import com.excentria_it.wamya.domain.JourneyRequestInputOutput;
import com.excentria_it.wamya.domain.JourneyRequestStatusCode;

public interface UpdateJourneyRequestPort {

	void updateJourneyRequest(JourneyRequestInputOutput build);

	void updateJourneyStatus(Set<Long> fulfilledJourneysIds, JourneyRequestStatusCode archived);

}
