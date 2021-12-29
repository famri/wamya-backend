package com.excentria_it.wamya.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TransporterProposalDto {
	private Long id;
	private Double price;
	private JourneyProposalStatusCode status;
	private JourneyRequestDto journey;
	private TransporterVehiculeDto vehicule;

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	@Builder
	public static class JourneyRequestDto {
		private Long id;

		private Place departurePlace;

		private Place arrivalPlace;

		private EngineType engineType;

		private Integer distance;

		private Integer hours;

		private Integer minutes;

		private LocalDateTime dateTime;

		private Integer workers;

		private String description;

		private Client client;

		@AllArgsConstructor
		@NoArgsConstructor
		@Data
		@Builder
		public static class EngineType {

			private Long id;

			private String name;

			private String code;

		}

		@AllArgsConstructor
		@NoArgsConstructor
		@Data
		@Builder
		public static class Place {

			private Long id;

			private String type;

			private String name;

			private BigDecimal latitude;

			private BigDecimal longitude;

			private Long departmentId;
		}

		@AllArgsConstructor
		@NoArgsConstructor
		@Data
		@Builder
		public static class Client {

			private Long id;

			private String firstname;

			private String photoUrl;
		}
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	@Builder
	public static class TransporterVehiculeDto {
		private Long id;

		private String regsitrationNumber;

		private LocalDate circulationDate;

		private String constructorName;

		private String modelName;

		private Long engineTypeId;

		private String engineTypeName;

		private String photoUrl;

	}
}
