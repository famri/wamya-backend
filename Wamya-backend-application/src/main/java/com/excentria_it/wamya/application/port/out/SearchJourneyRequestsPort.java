package com.excentria_it.wamya.application.port.out;

import java.time.LocalDateTime;
import java.util.Set;

import com.excentria_it.wamya.common.SortingCriterion;
import com.excentria_it.wamya.domain.JourneyRequestsSearchResult;

public interface SearchJourneyRequestsPort {

	JourneyRequestsSearchResult searchJourneyRequestsByDeparturePlaceRegionIdAndArrivalPlaceRegionIdAndEngineTypesAndDateBetween(
			String departurePlaceRegionId, Set<String> arrivalPlaceRegionId, Set<Long> engineTypes,
			LocalDateTime startDate, LocalDateTime endDate, Integer pageNumber, Integer pageSize,
			SortingCriterion sortingCriteria);

	JourneyRequestsSearchResult searchJourneyRequestsByDeparturePlaceRegionIdAndEngineTypesAndDateBetween(
			String departurePlaceRegionId, Set<Long> engineTypes, LocalDateTime startDate, LocalDateTime endDate,
			Integer pageNumber, Integer pageSize, SortingCriterion sortingCriteria);

}
