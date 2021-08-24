package com.excentria_it.wamya.application.service;

import java.time.ZoneId;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.LoadClientJourneyRequestsUseCase;
import com.excentria_it.wamya.application.port.out.LoadClientJourneyRequestsPort;
import com.excentria_it.wamya.application.utils.DateTimeHelper;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.domain.ClientJourneyRequestDto;
import com.excentria_it.wamya.domain.ClientJourneyRequests;
import com.excentria_it.wamya.domain.ClientJourneyRequestsOutput;
import com.excentria_it.wamya.domain.LoadClientJourneyRequestsCriteria;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class LoadJourneyRequestsService implements LoadClientJourneyRequestsUseCase {

	private final LoadClientJourneyRequestsPort loadClientJourneyRequestsPort;

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

}
