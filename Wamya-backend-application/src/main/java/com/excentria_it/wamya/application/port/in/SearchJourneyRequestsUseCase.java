package com.excentria_it.wamya.application.port.in;

import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.excentria_it.wamya.common.SortingCriterion;
import com.excentria_it.wamya.common.annotation.SortCriterion;
import com.excentria_it.wamya.domain.JourneyRequestsSearchResult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface SearchJourneyRequestsUseCase {

	JourneyRequestsSearchResult searchJourneyRequests(SearchJourneyRequestsCommand command, String locale);

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	class SearchJourneyRequestsCommand {

		public static final String ANY_ARRIVAL_REGION = "ANY";

		@NotNull
		private String departurePlaceRegionId;

		@NotNull
		@NotEmpty
		private Set<String> arrivalPlaceRegionIds;

		@NotNull
		private LocalDateTime startDateTime;
		@NotNull
		private LocalDateTime endDateTime;

		@NotNull
		@NotEmpty
		private Set<Long> engineTypes;

		@NotNull
		private Integer pageNumber;

		@NotNull
		private Integer pageSize;

		@SortCriterion(fields = { "min-price", "distance", "date-time" })
		private SortingCriterion sortingCriterion;

	}

}
