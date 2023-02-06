package com.excentria_it.wamya.application.port.in;

import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.Sort;
import com.excentria_it.wamya.domain.JourneyRequestStatusCode;
import com.excentria_it.wamya.domain.JourneyRequestsSearchResult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface SearchJourneyRequestsUseCase {

	JourneyRequestsSearchResult searchJourneyRequests(SearchJourneyRequestsCommand command, String userSubject,
			String locale);

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	class SearchJourneyRequestsCommand {

		public static final Long ANY_ARRIVAL_DEPARTMENT = -1L;

		@NotNull
		private Long departurePlaceDepartmentId;

		@NotNull
		@NotEmpty
		private Set<Long> arrivalPlaceDepartmentIds;

		@NotNull
//		@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
//		@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
		private LocalDateTime startDateTime;

		@NotNull
//		@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
//		@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
		private LocalDateTime endDateTime;

		@NotNull
		@NotEmpty
		private Set<Long> engineTypes;

		@NotNull
		private Integer pageNumber;

		@NotNull
		private Integer pageSize;

		@Sort(fields = { "distance", "date-time" })
		private SortCriterion sortingCriterion;

		private Set<JourneyRequestStatusCode> statusCodes;

	}

}
