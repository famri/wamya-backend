package com.excentria_it.wamya.application.port.in;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.excentria_it.wamya.domain.JourneyRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface CreateJourneyRequestUseCase {

	JourneyRequest createJourneyRequest(CreateJourneyRequestCommand command, String username);

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	class CreateJourneyRequestCommand {

		@NotNull
		private String departurePlaceId;

		@NotNull
		private String departurePlaceRegionId;

		@NotNull
		private String departurePlaceName;

		@NotNull
		private String arrivalPlaceId;

		@NotNull
		private String arrivalPlaceRegionId;

		@NotNull
		private String arrivalPlaceName;

		@NotNull
		private LocalDateTime dateTime;
		@NotNull
		private LocalDateTime endDateTime;

		@NotNull
		private Long engineTypeId;

		@NotNull
		private Double distance;

		@NotNull
		private Integer workers;

		@NotNull
		private String description;

	}
}
