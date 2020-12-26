package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.domain.JourneyRequestsSearchResult;
import com.excentria_it.wamya.domain.SearchJourneyRequestsCriteria;

public interface SearchJourneyRequestsPort {

	JourneyRequestsSearchResult searchJourneyRequests(SearchJourneyRequestsCriteria command);

}
