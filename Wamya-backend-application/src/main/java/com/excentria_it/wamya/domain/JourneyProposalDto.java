package com.excentria_it.wamya.domain;

import com.excentria_it.wamya.common.domain.StatusCode;

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

	private StatusDto status;

	private TransporterDto transporter;

	private VehiculeDto vehicule;

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

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class StatusDto {

		private StatusCode code;

		private String value;

	}

}
