package com.excentria_it.wamya.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class JourneyProposalDto {

	private Long id;

	private Double price;

	private TransporterDto transporterDto;

	private VehiculeDto vehiculeDto;

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class TransporterDto {

		private Long id;

		private String firstname;

		private String photoUrl;

		private Double globalRating;

	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class VehiculeDto {

		private Long id;

		private String constructor;

		private String model;

		private String photoUrl;

	}

}
