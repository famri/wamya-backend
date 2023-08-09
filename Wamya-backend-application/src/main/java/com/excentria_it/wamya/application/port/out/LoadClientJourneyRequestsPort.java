package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.domain.ClientJourneyRequestsOutput;
import com.excentria_it.wamya.domain.LoadClientJourneyRequestsCriteria;

public interface LoadClientJourneyRequestsPort {

	ClientJourneyRequestsOutput loadClientJourneyRequests(LoadClientJourneyRequestsCriteria criteria);

}
