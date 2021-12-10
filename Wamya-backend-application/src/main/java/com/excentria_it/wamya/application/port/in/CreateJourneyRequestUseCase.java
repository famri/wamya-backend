package com.excentria_it.wamya.application.port.in;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.excentria_it.wamya.common.annotation.Among;
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
		private Long departurePlaceId;

	
		@Among(value = {"geo-place", "department", "delegation", "locality"})
		private String departurePlaceType;

		@NotNull
		private Long arrivalPlaceId;

		@Among(value = { "geo-place", "department", "delegation", "locality"})
		private String arrivalPlaceType;

		@NotNull
		@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
		@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
		private LocalDateTime dateTime;

		@NotNull
		private Long engineTypeId;

		@NotNull
		private Integer workers;


		@NotEmpty
		private String description;

	}
}
