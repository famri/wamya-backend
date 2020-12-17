package com.excentria_it.wamya.adapter.persistence.adapter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.JpaSort;

import com.excentria_it.wamya.adapter.persistence.repository.JourneyRequestRepository;
import com.excentria_it.wamya.application.port.out.SearchJourneyRequestsPort;
import com.excentria_it.wamya.common.SortingCriterion;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.domain.JourneyRequestSearchDto;
import com.excentria_it.wamya.domain.JourneyRequestsSearchResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class JourneyRequestsPersistenceAdapter implements SearchJourneyRequestsPort {

	private final JourneyRequestRepository journeyRequestRepository;

	@Override
	public JourneyRequestsSearchResult searchJourneyRequestsByDeparturePlaceRegionIdAndArrivalPlaceRegionIdAndEngineTypesAndDateBetween(
			String departurePlaceRegionId, Set<String> arrivalPlaceRegionId, Set<Long> engineTypes,
			LocalDateTime startDate, LocalDateTime endDate, Integer pageNumber, Integer pageSize,
			SortingCriterion sortingCriterion) {

		Sort sort = convertToSort(sortingCriterion);

		Pageable pagingSort = PageRequest.of(pageNumber, pageSize, sort);

		Page<JourneyRequestSearchDto> journeyRequestsPage = journeyRequestRepository
				.findByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween(
						departurePlaceRegionId, arrivalPlaceRegionId, engineTypes, startDate, endDate, pagingSort);

		if (journeyRequestsPage != null) {

			return new JourneyRequestsSearchResult(journeyRequestsPage.getTotalPages(),
					journeyRequestsPage.getTotalElements(), journeyRequestsPage.getNumber(),
					journeyRequestsPage.getSize(), journeyRequestsPage.hasNext(), journeyRequestsPage.getContent());
		}

		return new JourneyRequestsSearchResult(0, 0, 0, 0, false, Collections.<JourneyRequestSearchDto>emptyList());
	}

	@Override
	public JourneyRequestsSearchResult searchJourneyRequestsByDeparturePlaceRegionIdAndEngineTypesAndDateBetween(
			String departurePlaceRegionId, Set<Long> engineTypes, LocalDateTime startDate, LocalDateTime endDate,
			Integer pageNumber, Integer pageSize, SortingCriterion sortingCriterion) {

		Sort sort = convertToSort(sortingCriterion);

		Pageable pagingSort = PageRequest.of(pageNumber, pageSize, sort);

		Page<JourneyRequestSearchDto> journeyRequestsPage = journeyRequestRepository
				.findByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween(departurePlaceRegionId, engineTypes,
						startDate, endDate, pagingSort);

		if (journeyRequestsPage != null) {

			return new JourneyRequestsSearchResult(journeyRequestsPage.getTotalPages(),
					journeyRequestsPage.getTotalElements(), journeyRequestsPage.getNumber(),
					journeyRequestsPage.getSize(), journeyRequestsPage.hasNext(), journeyRequestsPage.getContent());
		}

		return new JourneyRequestsSearchResult(0, 0, 0, 0, false, Collections.<JourneyRequestSearchDto>emptyList());
	}

	protected Sort convertToSort(SortingCriterion sortingCriterion) {
		if ("min-price".equals(sortingCriterion.getField())) {
			return (JpaSort.unsafe(Direction.valueOf(sortingCriterion.getDirection().toUpperCase()),
					stripDashes("(" + sortingCriterion.getField() + ")")));
		}
		return Sort.by(Direction.valueOf(sortingCriterion.getDirection().toUpperCase()),
				stripDashes(sortingCriterion.getField()));
	}

	protected String stripDashes(String str) {
		int idx;
		while ((idx = str.indexOf("-")) != -1) {
			if (idx < str.length() - 1) {
				String charToUpper = str.substring(idx + 1, idx + 2).toUpperCase();
				str = str.substring(0, idx) + charToUpper + str.substring(idx + 2);
			} else {
				str = str.substring(0, idx);
			}
		}
		return str;
	}
}
