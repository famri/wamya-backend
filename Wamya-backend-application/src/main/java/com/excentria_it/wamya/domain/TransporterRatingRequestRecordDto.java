package com.excentria_it.wamya.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TransporterRatingRequestRecordDto {

	private JourneyRequestDto journeyRequest;

	private TransporterDto transporter;

	private ClientDto client;
	
	private String hash;
	
	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	@Builder
	public static class JourneyRequestDto {

		private PlaceDto departurePlace;

		private PlaceDto arrivalPlace;

		private LocalDateTime dateTime;

	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	@Builder
	public static class TransporterDto {

		private String firstname;

		private String photoUrl;

		private Double globalRating;
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	@Builder
	public static class ClientDto {

		private Long id;
		
		private String firstname;
	
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class PlaceDto {

		private String name;

	}

}
