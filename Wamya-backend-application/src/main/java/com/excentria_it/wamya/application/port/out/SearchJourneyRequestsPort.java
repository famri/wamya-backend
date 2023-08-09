package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.domain.JourneyRequestsSearchOutputResult;
import com.excentria_it.wamya.domain.SearchJourneyRequestsInput;

public interface SearchJourneyRequestsPort {

	JourneyRequestsSearchOutputResult searchJourneyRequests(SearchJourneyRequestsInput command);

}
