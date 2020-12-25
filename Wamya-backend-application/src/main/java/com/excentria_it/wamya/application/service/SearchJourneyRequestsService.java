package com.excentria_it.wamya.application.service;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.SearchJourneyRequestsUseCase;
import com.excentria_it.wamya.application.port.out.SearchJourneyRequestsPort;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.domain.JourneyRequestsSearchResult;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class SearchJourneyRequestsService implements SearchJourneyRequestsUseCase {

	private final SearchJourneyRequestsPort searchJourneyRequestsPort;

	@Override
	public JourneyRequestsSearchResult searchJourneyRequests(SearchJourneyRequestsCommand command, String locale) {

		if (isArrivalPlaceRegionAgnostic(command)) {

			return searchJourneyRequestsPort.searchJourneyRequestsByDeparturePlaceRegionIdAndEngineTypesAndDateBetween(
					command.getDeparturePlaceRegionId(), command.getEngineTypes(), command.getStartDateTime(),
					command.getEndDateTime(), locale, command.getPageNumber(), command.getPageSize(),
					command.getSortingCriterion());
		} else {
			return searchJourneyRequestsPort
					.searchJourneyRequestsByDeparturePlaceRegionIdAndArrivalPlaceRegionIdAndEngineTypesAndDateBetween(
							command.getDeparturePlaceRegionId(), command.getArrivalPlaceRegionIds(),
							command.getEngineTypes(), command.getStartDateTime(), command.getEndDateTime(), locale,
							command.getPageNumber(), command.getPageSize(), command.getSortingCriterion());
		}

	}

	protected boolean isArrivalPlaceRegionAgnostic(SearchJourneyRequestsCommand command) {
		return command.getArrivalPlaceRegionIds().stream()
				.anyMatch(p -> SearchJourneyRequestsCommand.ANY_ARRIVAL_REGION.equals(p.toUpperCase()));
	}

}
