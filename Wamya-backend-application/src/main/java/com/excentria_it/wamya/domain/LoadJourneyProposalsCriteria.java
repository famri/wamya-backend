package com.excentria_it.wamya.domain;

import java.util.List;

import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.domain.StatusCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoadJourneyProposalsCriteria {

	private Long journeyRequestId;

	private String clientUsername;

	private SortCriterion sortingCriterion;

	private List<StatusCode> statusCodes;
}
