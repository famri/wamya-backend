package com.excentria_it.wamya.domain;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class TransporterRatingRequestRecordOutput {

	private Long id;

	private JourneyRequestOutput journeyRequest;

	private TransporterDto transporter;

	private ClientDto client;

	private String hash;

	@AllArgsConstructor
	@Data
	@Builder
	public static class JourneyRequestOutput {

		private PlaceDto departurePlace;

		private PlaceDto arrivalPlace;

		private Instant dateTime;

	}

	@AllArgsConstructor
	@Data
	@Builder
	public static class TransporterDto {

		private String firstname;

		private String photoUrl;

		private Double globalRating;
	}

	@AllArgsConstructor
	@Data
	@Builder
	public static class ClientDto {

		private Long id;

		private String firstname;

		private String email;

		private String locale;

		private String timeZone;

	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class PlaceDto {

		private String name;

	}
}
