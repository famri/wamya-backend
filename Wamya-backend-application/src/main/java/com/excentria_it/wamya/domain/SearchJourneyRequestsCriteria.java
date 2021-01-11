package com.excentria_it.wamya.domain;

import java.time.Instant;
import java.util.Set;

import com.excentria_it.wamya.common.SortCriterion;

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

	private Instant startDateTime;

	private Instant endDateTime;

	private Set<Long> engineTypes;

	private Integer pageNumber;

	private Integer pageSize;

	private SortCriterion sortingCriterion;

	private String locale;
}
