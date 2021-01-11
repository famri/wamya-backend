package com.excentria_it.wamya.application.port.in;

import java.time.ZonedDateTime;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.excentria_it.wamya.domain.CreateJourneyRequestDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface CreateJourneyRequestUseCase {

	CreateJourneyRequestDto createJourneyRequest(CreateJourneyRequestCommand command, String username, String locale);

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
		@DateTimeFormat(iso = ISO.DATE_TIME)
		@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
		private ZonedDateTime dateTime;

		@NotNull
		@DateTimeFormat(iso = ISO.DATE_TIME)
		@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
		private ZonedDateTime endDateTime;

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
