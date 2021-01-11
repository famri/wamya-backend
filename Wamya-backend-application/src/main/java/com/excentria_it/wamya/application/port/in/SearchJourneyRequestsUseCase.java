package com.excentria_it.wamya.application.port.in;

import java.time.ZonedDateTime;
import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.Sort;
import com.excentria_it.wamya.domain.JourneyRequestsSearchResult;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

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
		@DateTimeFormat(iso = ISO.DATE_TIME)
		@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
		private ZonedDateTime startDateTime;
		@NotNull
		@DateTimeFormat(iso = ISO.DATE_TIME)
		@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
		private ZonedDateTime endDateTime;

		@NotNull
		@NotEmpty
		private Set<Long> engineTypes;

		@NotNull
		private Integer pageNumber;

		@NotNull
		private Integer pageSize;

		@Sort(fields = { "min-price", "distance", "date-time" })
		private SortCriterion sortingCriterion;

	}

}
