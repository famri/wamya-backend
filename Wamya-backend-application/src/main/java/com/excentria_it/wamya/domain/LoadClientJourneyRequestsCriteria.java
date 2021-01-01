package com.excentria_it.wamya.domain;

import com.excentria_it.wamya.common.PeriodCriterion;
import com.excentria_it.wamya.common.SortCriterion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoadClientJourneyRequestsCriteria {

	private String clientUsername;

	private Integer pageNumber;

	private Integer pageSize;

	private SortCriterion sortingCriterion;

	private PeriodCriterion periodCriterion;
}
