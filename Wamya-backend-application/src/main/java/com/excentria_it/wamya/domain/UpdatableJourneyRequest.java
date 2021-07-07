package com.excentria_it.wamya.domain;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatableJourneyRequest {
	private Long departurePlaceId;

	private String departurePlaceType;

	private Long arrivalPlaceId;

	private String arrivalPlaceType;

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
	private LocalDateTime dateTime;

	private Long engineTypeId;

	private Integer workers;

	private String description;

	private String  status;
}
