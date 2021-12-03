package com.excentria_it.wamya.domain;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
public class JourneyRequestOutput {

	private Place departurePlace;

	private Place arrivalPlace;

	private Instant dateTime;

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class Place {

		private String name;

	}

}
