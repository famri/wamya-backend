package com.excentria_it.wamya.domain;

import java.util.Set;

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
public class LoadTransporterProposalsCriteria {

	private String transporterUsername;

	private Integer pageNumber;

	private Integer pageSize;

	private Set<JourneyProposalStatusCode> statusCodes;

	private SortCriterion sortingCriterion;

	private PeriodCriterion periodCriterion;

}
