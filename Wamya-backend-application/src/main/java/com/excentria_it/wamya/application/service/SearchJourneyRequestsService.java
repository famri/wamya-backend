package com.excentria_it.wamya.application.service;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.SearchJourneyRequestsUseCase;
import com.excentria_it.wamya.application.port.out.SearchJourneyRequestsPort;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.domain.JourneyRequestsSearchResult;
import com.excentria_it.wamya.domain.SearchJourneyRequestsCriteria;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class SearchJourneyRequestsService implements SearchJourneyRequestsUseCase {

	private final SearchJourneyRequestsPort searchJourneyRequestsPort;

	@Override
	public JourneyRequestsSearchResult searchJourneyRequests(SearchJourneyRequestsCommand command, String locale) {

		SearchJourneyRequestsCriteria searchCriteria = new SearchJourneyRequestsCriteria(
				command.getDeparturePlaceDepartmentId(), command.getArrivalPlaceDepartmentIds(),
				command.getStartDateTime().toInstant(), command.getEndDateTime().toInstant(), command.getEngineTypes(),
				command.getPageNumber(), command.getPageSize(), command.getSortingCriterion(), locale);

		return searchJourneyRequestsPort.searchJourneyRequests(searchCriteria);

	}

}
