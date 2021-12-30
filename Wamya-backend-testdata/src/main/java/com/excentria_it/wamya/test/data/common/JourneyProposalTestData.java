package com.excentria_it.wamya.test.data.common;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.excentria_it.wamya.application.port.in.LoadProposalsUseCase.LoadProposalsCommand;
import com.excentria_it.wamya.application.port.in.LoadProposalsUseCase.LoadProposalsCommand.LoadProposalsCommandBuilder;
import com.excentria_it.wamya.application.port.in.LoadTransporterProposalsUseCase.LoadTransporterProposalsCommand;
import com.excentria_it.wamya.application.port.in.LoadTransporterProposalsUseCase.LoadTransporterProposalsCommand.LoadTransporterProposalsCommandBuilder;
import com.excentria_it.wamya.application.port.in.MakeProposalUseCase.MakeProposalCommand;
import com.excentria_it.wamya.application.port.in.MakeProposalUseCase.MakeProposalCommand.MakeProposalCommandBuilder;
import com.excentria_it.wamya.common.PeriodCriterion;
import com.excentria_it.wamya.common.PeriodCriterion.PeriodValue;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.domain.StatusCode;
import com.excentria_it.wamya.domain.JourneyProposalDto;
import com.excentria_it.wamya.domain.JourneyProposalDto.StatusDto;
import com.excentria_it.wamya.domain.JourneyProposalDto.TransporterDto;
import com.excentria_it.wamya.domain.JourneyProposalDto.VehiculeDto;
import com.excentria_it.wamya.domain.JourneyProposalStatusCode;
import com.excentria_it.wamya.domain.JourneyRequestProposals;
import com.excentria_it.wamya.domain.LoadJourneyProposalsCriteria;
import com.excentria_it.wamya.domain.LoadJourneyProposalsCriteria.LoadJourneyProposalsCriteriaBuilder;
import com.excentria_it.wamya.domain.MakeProposalDto;
import com.excentria_it.wamya.domain.TransporterProposalDto;
import com.excentria_it.wamya.domain.TransporterProposalDto.JourneyRequestDto;
import com.excentria_it.wamya.domain.TransporterProposalDto.TransporterVehiculeDto;
import com.excentria_it.wamya.domain.TransporterProposalOutput;
import com.excentria_it.wamya.domain.TransporterProposalOutput.JourneyRequestOutput;
import com.excentria_it.wamya.domain.TransporterProposalOutput.JourneyRequestOutput.ClientOutput;
import com.excentria_it.wamya.domain.TransporterProposalOutput.JourneyRequestOutput.EngineTypeDto;
import com.excentria_it.wamya.domain.TransporterProposalOutput.JourneyRequestOutput.PlaceDto;
import com.excentria_it.wamya.domain.TransporterProposalOutput.TransporterVehiculeOutput;
import com.excentria_it.wamya.domain.TransporterProposals;
import com.excentria_it.wamya.domain.TransporterProposalsOutput;;

public class JourneyProposalTestData {
	private static final List<JourneyProposalDto> journeyProposalDtos = List.of(
			JourneyProposalDto.builder().id(1L).price(220.0).status(new StatusDto(StatusCode.SUBMITTED, "submitted"))
					.transporter(new TransporterDto(1L, "transporter1", "https://path/to/transporter1/photo", 4.5))
					.vehicule(new VehiculeDto(1L, "RENAULT", "KANGOO", "https://path/to/vehicule1/photo")).build(),
			JourneyProposalDto.builder().id(2L).price(230.0).status(new StatusDto(StatusCode.SUBMITTED, "submitted"))
					.transporter(new TransporterDto(2L, "transporter2", "https://path/to/transporter2/photo", 4.9))
					.vehicule(new VehiculeDto(2L, "PEUGEOT", "PARTNER", "https://path/to/vehicule2/photo")).build(),
			JourneyProposalDto.builder().id(2L).price(240.0).status(new StatusDto(StatusCode.SUBMITTED, "submitted"))
					.transporter(new TransporterDto(3L, "transporter3", "https://path/to/transporter3/photo", 4.7))
					.vehicule(new VehiculeDto(3L, "PEUGEOT", "PARTNER", "https://path/to/vehicule3/photo")).build());

	public static final Double JOURNEY_PROPOSAL_PRICE = 250.0;

	public static final MakeProposalCommandBuilder defaultMakeProposalCommandBuilder() {
		return MakeProposalCommand.builder().price(JOURNEY_PROPOSAL_PRICE).vehiculeId(1L);
	}

	public static final LoadProposalsCommandBuilder defaultLoadProposalsCommandBuilder() {
		return LoadProposalsCommand.builder().clientUsername(TestConstants.DEFAULT_EMAIL).journeyRequestId(1L)
				.pageNumber(0).pageSize(25).sortingCriterion(new SortCriterion("price", "asc"));
	}

	public static final LoadTransporterProposalsCommandBuilder defaultLoadTransporterProposalsCommandBuilder() {
		ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
		ZonedDateTime[] edges = PeriodValue.LM3.calculateLowerAndHigherEdges(now);

		return LoadTransporterProposalsCommand.builder().transporterUsername(TestConstants.DEFAULT_EMAIL)
				.statusCodes(Arrays.stream(JourneyProposalStatusCode.values()).collect(Collectors.toSet()))

				.pageNumber(0).pageSize(25).sortingCriterion(new SortCriterion("price", "desc"))
				.periodCriterion(new PeriodCriterion("lm3", edges[0], edges[1]));
	}

	public static final JourneyRequestProposals defaultJourneyRequestProposals() {
		return new JourneyRequestProposals(3, journeyProposalDtos);
	}

	public static final JourneyProposalDto defaultJourneyProposalDto() {
		return journeyProposalDtos.get(0);
	}

	public static final LoadJourneyProposalsCriteriaBuilder defaultLoadJourneyProposalsCriteriaBuilder() {
		return LoadJourneyProposalsCriteria.builder().clientUsername(TestConstants.DEFAULT_EMAIL).journeyRequestId(1L)
				.sortingCriterion(new SortCriterion("price", "asc"))
				.statusCodes(List.of(StatusCode.SUBMITTED, StatusCode.ACCEPTED));
	}

	public static MakeProposalDto defaultMakeProposalDto() {
		return new MakeProposalDto(1L, 250.0, "submitted");
	}

	private static final List<TransporterProposalOutput> transporterProposalOutputs = List.of(
			TransporterProposalOutput
					.builder().id(1L).price(250.0).status("Accepté").statusCode(JourneyProposalStatusCode.ACCEPTED)
					.vehicule(
							TransporterVehiculeOutput.builder().id(1L).registrationNumber("1 TUN 220")
									.circulationDate(LocalDate.of(2020, 01, 01)).constructorName("PEUGEOT")
									.modelName("PARTNER").engineTypeId(1L).engineTypeName("Véhicule Utilitaire")
									.imageHash("VEHICULE_1_HASH").imageId(1L).build())
					.journey(JourneyRequestOutput.builder().id(1L)
							.departurePlace(PlaceDto.builder().id(1L).type("DEPARTMENT").name("Sfax")
									.latitude(new BigDecimal(38.0)).longitude(new BigDecimal(10.0)).departmentId(1L)
									.build())
							.arrivalPlace(PlaceDto.builder().id(2L).type("DEPARTMENT").name("Tunis")
									.latitude(new BigDecimal(37.0)).longitude(new BigDecimal(11.0)).departmentId(2L)
									.build())
							.engineType(
									EngineTypeDto.builder().id(1L).name("Véhicule Utilitaire").code("UTILITY").build())
							.distance(270).hours(3).minutes(30).dateTime(Instant.now().plus(30, ChronoUnit.DAYS))
							.workers(2)
							.description(
									"Besoin de transporteur avec 2 manutentionnaires de pour déménagement de sfax à tunis")
							.client(ClientOutput.builder().id(1L).firstname("Client1").imageHash("CLIENT_1_IMAGE_HASH")
									.imageId(11L).build())
							.build())
					.build(),

			TransporterProposalOutput.builder().id(2L).price(280.0).status("Envoyé").statusCode(JourneyProposalStatusCode.SUBMITTED)
					.vehicule(TransporterVehiculeOutput.builder().id(2L).registrationNumber("2 TUN 220")
							.circulationDate(LocalDate.of(2020, 01, 01)).constructorName("CITROEN").modelName("NEMO")
							.engineTypeId(1L).engineTypeName("Véhicule Utilitaire").imageHash("VEHICULE_2_HASH")
							.imageId(2L).build())
					.journey(JourneyRequestOutput.builder().id(2L)
							.departurePlace(PlaceDto.builder().id(3L).type("DEPARTMENT").name("Sousse")
									.latitude(new BigDecimal(39.0)).longitude(new BigDecimal(11.5)).departmentId(3L)
									.build())
							.arrivalPlace(PlaceDto.builder().id(4L).type("DEPARTMENT").name("Bizerte")
									.latitude(new BigDecimal(37.5)).longitude(new BigDecimal(11.5)).departmentId(4L)
									.build())
							.engineType(
									EngineTypeDto.builder().id(1L).name("Véhicule Utilitaire").code("UTILITY").build())
							.distance(210).hours(2).minutes(45).dateTime(Instant.now().plus(15, ChronoUnit.DAYS))
							.workers(2)
							.description(
									"Besoin de transporteur avec 2 manutentionnaires de pour déménagement de sousse à bizerte")
							.client(ClientOutput.builder().id(2L).firstname("Client2").imageHash("CLIENT_2_IMAGE_HASH")
									.imageId(12L).build())
							.build())
					.build());

	private static final List<TransporterProposalDto> transporterProposals = List.of(
			TransporterProposalDto.builder().id(1L).price(250.0).status("Accepté").statusCode(JourneyProposalStatusCode.ACCEPTED)
					.vehicule(TransporterVehiculeDto.builder().id(1L).registrationNumber("1 TUN 220")
							.circulationDate(LocalDate.of(2020, 01, 01)).constructorName("PEUGEOT").modelName("PARTNER")
							.engineTypeId(1L).engineTypeName("Véhicule Utilitaire")
							.photoUrl("https://vehicule1/photo/url").build())
					.journey(JourneyRequestDto.builder().id(1L)
							.departurePlace(JourneyRequestDto.Place
									.builder().id(1L).type("DEPARTMENT").name("Sfax").latitude(new BigDecimal(38.0))
									.longitude(new BigDecimal(10.0)).departmentId(1L).build())
							.arrivalPlace(JourneyRequestDto.Place.builder().id(2L).type("DEPARTMENT").name("Tunis")
									.latitude(new BigDecimal(37.0)).longitude(new BigDecimal(11.0)).departmentId(2L)
									.build())
							.engineType(JourneyRequestDto.EngineType
									.builder().id(1L).name("Véhicule Utilitaire").code("UTILITY").build())
							.distance(270).hours(3).minutes(30)
							.dateTime(Instant
									.now().plus(30, ChronoUnit.DAYS).atZone(ZoneId.of("Africa/Tunis"))
									.toLocalDateTime())
							.workers(2)
							.description(
									"Besoin de transporteur avec 2 manutentionnaires de pour déménagement de sfax à tunis")
							.client(JourneyRequestDto.Client.builder().id(1L).firstname("Client1")
									.photoUrl("https://client1/photo/url").build())
							.build())
					.build(),

			TransporterProposalDto.builder().id(2L).price(250.0).status("Envoyé").statusCode(JourneyProposalStatusCode.SUBMITTED)
					.vehicule(TransporterVehiculeDto.builder().id(1L).registrationNumber("2 TUN 220")
							.circulationDate(LocalDate.of(2020, 01, 01)).constructorName("CITROEN").modelName("NEMO")
							.engineTypeId(1L).engineTypeName("Véhicule Utilitaire")
							.photoUrl("https://vehicule2/photo/url").build())
					.journey(JourneyRequestDto.builder().id(2L)
							.departurePlace(JourneyRequestDto.Place.builder().id(3L).type("DEPARTMENT").name("Sousse")
									.latitude(new BigDecimal(39.0)).longitude(new BigDecimal(11.0)).departmentId(1L)
									.build())
							.arrivalPlace(JourneyRequestDto.Place.builder().id(4L).type("DEPARTMENT").name("Bizerte")
									.latitude(new BigDecimal(38.0)).longitude(new BigDecimal(12.0)).departmentId(2L)
									.build())
							.engineType(JourneyRequestDto.EngineType
									.builder().id(1L).name("Véhicule Utilitaire").code("UTILITY").build())
							.distance(210).hours(2).minutes(45)
							.dateTime(Instant.now().plus(15, ChronoUnit.DAYS).atZone(ZoneId.of("Africa/Tunis"))
									.toLocalDateTime())
							.workers(2)
							.description(
									"Besoin de transporteur avec 2 manutentionnaires de pour déménagement de sousse à bizerte")
							.client(JourneyRequestDto.Client.builder().id(2L).firstname("Client2")
									.photoUrl("https://client2/photo/url").build())
							.build())
					.build());

	public static List<TransporterProposalOutput> defaultTransporterProposalOutputList() {

		return transporterProposalOutputs;
	}

	public static List<TransporterProposalDto> defaultTransporterProposalsList() {

		return transporterProposals;
	}

	public static TransporterProposalsOutput defaultTransporterProposalsOutput() {

		return TransporterProposalsOutput.builder().totalPages(1).totalElements(2).pageNumber(0).pageSize(25)
				.hasNext(false).content(transporterProposalOutputs).build();
	}

	public static TransporterProposals defaultTransporterProposals() {

		return TransporterProposals.builder().totalPages(1).totalElements(2).pageNumber(0).pageSize(25).hasNext(false)
				.content(transporterProposals).build();
	}

}
