package com.excentria_it.wamya.domain;

import java.time.LocalDateTime;
import java.util.Set;

import com.excentria_it.wamya.common.SortingCriterion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SearchJourneyRequestsCriteria {

	private String departurePlaceRegionId;

	private Set<String> arrivalPlaceRegionIds;

	private LocalDateTime startDateTime;

	private LocalDateTime endDateTime;

	private Set<Long> engineTypes;

	private Integer pageNumber;

	private Integer pageSize;

	private SortingCriterion sortingCriterion;

	private String locale;
}
