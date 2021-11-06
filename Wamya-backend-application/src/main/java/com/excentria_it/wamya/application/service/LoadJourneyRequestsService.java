package com.excentria_it.wamya.application.service;

import java.time.ZoneId;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.LoadClientJourneyRequestsUseCase;
import com.excentria_it.wamya.application.port.out.LoadClientJourneyRequestsPort;
import com.excentria_it.wamya.application.port.out.LoadJourneyRequestPort;
import com.excentria_it.wamya.application.utils.DateTimeHelper;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.exception.JourneyRequestNotFoundException;
import com.excentria_it.wamya.domain.ClientJourneyRequestDto;
import com.excentria_it.wamya.domain.ClientJourneyRequestDtoOutput;
import com.excentria_it.wamya.domain.ClientJourneyRequests;
import com.excentria_it.wamya.domain.ClientJourneyRequestsOutput;
import com.excentria_it.wamya.domain.LoadClientJourneyRequestsCriteria;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class LoadJourneyRequestsService implements LoadClientJourneyRequestsUseCase {

	private final LoadClientJourneyRequestsPort loadClientJourneyRequestsPort;

	private final LoadJourneyRequestPort loadJourneyRequestPort;

	private final DateTimeHelper dateTimeHelper;

	@Override
	public ClientJourneyRequests loadJourneyRequests(LoadJourneyRequestsCommand command, String locale) {

		LoadClientJourneyRequestsCriteria loadingCriteria = LoadClientJourneyRequestsCriteria.builder()
				.clientUsername(command.getClientUsername()).periodCriterion(command.getPeriodCriterion())
				.sortingCriterion(command.getSortingCriterion()).pageNumber(command.getPageNumber())
				.pageSize(command.getPageSize()).locale(locale).build();

		ZoneId userZoneId = dateTimeHelper.findUserZoneId(command.getClientUsername());

		ClientJourneyRequestsOutput clientJourneyRequestsOutput = loadClientJourneyRequestsPort
				.loadClientJourneyRequests(loadingCriteria);

		ClientJourneyRequests result = new ClientJourneyRequests();

		result.setTotalPages(clientJourneyRequestsOutput.getTotalPages());
		result.setTotalElements(clientJourneyRequestsOutput.getTotalPages());
		result.setPageNumber(clientJourneyRequestsOutput.getPageNumber());
		result.setPageSize(clientJourneyRequestsOutput.getPageSize());
		result.setHasNext(clientJourneyRequestsOutput.isHasNext());

		result.setContent(clientJourneyRequestsOutput.getContent().stream().map(e -> new ClientJourneyRequestDto(
				e.getId(),
				new ClientJourneyRequestDto.PlaceDto(e.getDeparturePlace().getId(), e.getDeparturePlace().getType(),
						e.getDeparturePlace().getName(), e.getDeparturePlace().getLatitude(),
						e.getDeparturePlace().getLongitude(), e.getDeparturePlace().getDepartmentId()),
				new ClientJourneyRequestDto.PlaceDto(e.getArrivalPlace().getId(), e.getArrivalPlace().getType(),
						e.getArrivalPlace().getName(), e.getArrivalPlace().getLatitude(),
						e.getArrivalPlace().getLongitude(), e.getArrivalPlace().getDepartmentId()),
				new ClientJourneyRequestDto.EngineTypeDto(e.getEngineType().getId(), e.getEngineType().getName(),
						e.getEngineType().getCode()),
				e.getDistance(), dateTimeHelper.systemToUserLocalDateTime(e.getDateTime(), userZoneId),
				e.getCreationDateTime(), e.getWorkers(), e.getDescription(), e.getProposalsCount()

		)).collect(Collectors.toList()));

		return result;
	}

	@Override
	public ClientJourneyRequestDto loadJourneyRequest(LoadJourneyRequestCommand command, String locale) {

		ZoneId userZoneId = dateTimeHelper.findUserZoneId(command.getClientUsername());

		Optional<ClientJourneyRequestDtoOutput> cjrOutputDtoOptional = loadJourneyRequestPort
				.loadJourneyRequestByIdAndClientEmail(command.getJourneyRequestId(), command.getClientUsername(),
						locale);

		if (cjrOutputDtoOptional.isEmpty()) {
			throw new JourneyRequestNotFoundException(
					String.format("Journey request not found: %d", command.getJourneyRequestId()));
		}

		return new ClientJourneyRequestDto(cjrOutputDtoOptional.get().getId(),
				new ClientJourneyRequestDto.PlaceDto(cjrOutputDtoOptional.get().getDeparturePlace().getId(),
						cjrOutputDtoOptional.get().getDeparturePlace().getType(),
						cjrOutputDtoOptional.get().getDeparturePlace().getName(),
						cjrOutputDtoOptional.get().getDeparturePlace().getLatitude(),
						cjrOutputDtoOptional.get().getDeparturePlace().getLongitude(),
						cjrOutputDtoOptional.get().getDeparturePlace().getDepartmentId()),
				new ClientJourneyRequestDto.PlaceDto(cjrOutputDtoOptional.get().getArrivalPlace().getId(),
						cjrOutputDtoOptional.get().getArrivalPlace().getType(),
						cjrOutputDtoOptional.get().getArrivalPlace().getName(),
						cjrOutputDtoOptional.get().getArrivalPlace().getLatitude(),
						cjrOutputDtoOptional.get().getArrivalPlace().getLongitude(),
						cjrOutputDtoOptional.get().getArrivalPlace().getDepartmentId()),
				new ClientJourneyRequestDto.EngineTypeDto(cjrOutputDtoOptional.get().getEngineType().getId(),
						cjrOutputDtoOptional.get().getEngineType().getName(),
						cjrOutputDtoOptional.get().getEngineType().getCode()),
				cjrOutputDtoOptional.get().getDistance(),
				dateTimeHelper.systemToUserLocalDateTime(cjrOutputDtoOptional.get().getDateTime(), userZoneId),
				cjrOutputDtoOptional.get().getCreationDateTime(), cjrOutputDtoOptional.get().getWorkers(),
				cjrOutputDtoOptional.get().getDescription(), cjrOutputDtoOptional.get().getProposalsCount()

		);

	}

}
