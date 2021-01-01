package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.domain.ClientJourneyRequests;
import com.excentria_it.wamya.domain.LoadClientJourneyRequestsCriteria;

public interface LoadClientJourneyRequestsPort {

	ClientJourneyRequests loadClientJourneyRequests(LoadClientJourneyRequestsCriteria criteria);

}
