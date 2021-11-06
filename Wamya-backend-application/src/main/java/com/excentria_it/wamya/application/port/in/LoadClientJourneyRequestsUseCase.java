package com.excentria_it.wamya.application.port.in;

import javax.validation.constraints.NotNull;

import com.excentria_it.wamya.common.PeriodCriterion;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.Period;
import com.excentria_it.wamya.common.annotation.Sort;
import com.excentria_it.wamya.domain.ClientJourneyRequestDto;
import com.excentria_it.wamya.domain.ClientJourneyRequests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface LoadClientJourneyRequestsUseCase {

	ClientJourneyRequests loadJourneyRequests(LoadJourneyRequestsCommand command, String locale);

	ClientJourneyRequestDto loadJourneyRequest(LoadJourneyRequestCommand command, String locale);

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	class LoadJourneyRequestsCommand {
		@NotNull
		private String clientUsername;
		@NotNull
		private Integer pageNumber;
		@NotNull
		private Integer pageSize;

		@Sort(fields = { "creation-date-time", "date-time" })
		private SortCriterion sortingCriterion;

		@Period(value = { "y1", "m6", "m3", "m1", "w1" })
		private PeriodCriterion periodCriterion;
	}

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	class LoadJourneyRequestCommand {
		@NotNull
		private String clientUsername;
		@NotNull
		private Long journeyRequestId;
	}
}
