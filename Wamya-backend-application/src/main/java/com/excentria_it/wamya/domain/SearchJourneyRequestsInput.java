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
public class SearchJourneyRequestsInput {

	private Long departurePlaceDepartmentId;

	private Set<Long> arrivalPlaceDepartmentIds;

	private Instant startDateTime;

	private Instant endDateTime;

	private Set<Long> engineTypes;

	private Integer pageNumber;

	private Integer pageSize;

	private SortCriterion sortingCriterion;

	private Set<JourneyRequestStatusCode> statusCodes;

	private String locale;
}
