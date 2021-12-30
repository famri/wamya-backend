package com.excentria_it.wamya.application.service;

import java.time.ZoneId;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.LoadTransporterProposalsUseCase;
import com.excentria_it.wamya.application.port.out.LoadTransporterProposalsPort;
import com.excentria_it.wamya.application.utils.DateTimeHelper;
import com.excentria_it.wamya.application.utils.DocumentUrlResolver;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.domain.LoadTransporterProposalsCriteria;
import com.excentria_it.wamya.domain.TransporterProposalDto;
import com.excentria_it.wamya.domain.TransporterProposalDto.JourneyRequestDto;
import com.excentria_it.wamya.domain.TransporterProposalDto.TransporterVehiculeDto;
import com.excentria_it.wamya.domain.TransporterProposalOutput;
import com.excentria_it.wamya.domain.TransporterProposals;
import com.excentria_it.wamya.domain.TransporterProposalsOutput;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class LoadTransporterProposalsService implements LoadTransporterProposalsUseCase {

	private final LoadTransporterProposalsPort loadTransporterPropsalsPort;
	private final DateTimeHelper dateTimeHelper;
	private final DocumentUrlResolver documentUrlResolver;

	@Override
	public TransporterProposals loadProposals(LoadTransporterProposalsCommand command, String locale) {

		LoadTransporterProposalsCriteria criteria = LoadTransporterProposalsCriteria.builder()
				.transporterUsername(command.getTransporterUsername()).pageNumber(command.getPageNumber())
				.pageSize(command.getPageSize()).statusCodes(command.getStatusCodes())
				.sortingCriterion(command.getSortingCriterion()).periodCriterion(command.getPeriodCriterion()).build();

		TransporterProposalsOutput proposalsOutput = loadTransporterPropsalsPort.loadTransporterProposals(criteria,
				locale);

		ZoneId transporterZoneId = dateTimeHelper.findUserZoneId(command.getTransporterUsername());

		return TransporterProposals.builder().totalPages(proposalsOutput.getTotalPages())
				.totalElements(proposalsOutput.getTotalElements()).pageNumber(proposalsOutput.getPageNumber())
				.pageSize(proposalsOutput.getPageSize())

				.hasNext(proposalsOutput.isHasNext())
				.content(proposalsOutput.getContent().stream()
						.map(po -> mapTransporterProposalOutputToTransporterProposalDto(po, transporterZoneId))
						.collect(Collectors.toList()))
				.build();
	}

	private TransporterProposalDto mapTransporterProposalOutputToTransporterProposalDto(TransporterProposalOutput po,
			ZoneId transporterZoneId) {

		TransporterProposalDto.TransporterVehiculeDto vehicule = TransporterVehiculeDto.builder()
				.id(po.getVehicule().getId()).registrationNumber(po.getVehicule().getRegistrationNumber())
				.circulationDate(po.getVehicule().getCirculationDate())
				.constructorName(po.getVehicule().getConstructorName()).modelName(po.getVehicule().getModelName())
				.engineTypeId(po.getVehicule().getEngineTypeId()).engineTypeName(po.getVehicule().getEngineTypeName())
				.photoUrl(
						documentUrlResolver.resolveUrl(po.getVehicule().getImageId(), po.getVehicule().getImageHash()))
				.build();

		JourneyRequestDto.Place departurePlace = new JourneyRequestDto.Place(
				po.getJourney().getDeparturePlace().getId(), po.getJourney().getDeparturePlace().getType(),
				po.getJourney().getDeparturePlace().getName(), po.getJourney().getDeparturePlace().getLatitude(),
				po.getJourney().getDeparturePlace().getLongitude(),
				po.getJourney().getDeparturePlace().getDepartmentId());

		JourneyRequestDto.Place arrivalPlace = new JourneyRequestDto.Place(po.getJourney().getArrivalPlace().getId(),
				po.getJourney().getArrivalPlace().getType(), po.getJourney().getArrivalPlace().getName(),
				po.getJourney().getArrivalPlace().getLatitude(), po.getJourney().getArrivalPlace().getLongitude(),
				po.getJourney().getArrivalPlace().getDepartmentId());

		JourneyRequestDto.EngineType engineType = new JourneyRequestDto.EngineType(
				po.getJourney().getEngineType().getId(), po.getJourney().getEngineType().getName(),
				po.getJourney().getEngineType().getCode());

		JourneyRequestDto.Client client = new JourneyRequestDto.Client(po.getJourney().getClient().getId(),
				po.getJourney().getClient().getFirstname(), documentUrlResolver.resolveUrl(
						po.getJourney().getClient().getImageId(), po.getJourney().getClient().getImageHash()));

		JourneyRequestDto journey = JourneyRequestDto.builder().id(po.getJourney().getId())
				.departurePlace(departurePlace).arrivalPlace(arrivalPlace).engineType(engineType)
				.distance(po.getJourney().getDistance()).hours(po.getJourney().getHours())
				.minutes(po.getJourney().getMinutes())
				.dateTime(dateTimeHelper.systemToUserLocalDateTime(po.getJourney().getDateTime(), transporterZoneId))
				.workers(po.getJourney().getWorkers()).description(po.getJourney().getDescription()).client(client)
				.build();

		return TransporterProposalDto.builder().id(po.getId()).price(po.getPrice()).status(po.getStatus())
				.statusCode(po.getStatusCode()).vehicule(vehicule).journey(journey).build();
	}

}
