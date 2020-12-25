package com.excentria_it.wamya.application.service;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.CreateJourneyRequestUseCase;
import com.excentria_it.wamya.application.port.out.CreateJourneyRequestPort;
import com.excentria_it.wamya.application.utils.MapperUtility;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.domain.JourneyRequest;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class CreateJourneyRequestService implements CreateJourneyRequestUseCase {
	
	private final CreateJourneyRequestPort createJourneyRequestPort;

	@Override
	public JourneyRequest createJourneyRequest(CreateJourneyRequestCommand command, String username) {

		JourneyRequest journeyRequest = JourneyRequest.builder()
				.departurePlace(MapperUtility.buildPlaceDto(command.getDeparturePlaceId(),
						command.getDeparturePlaceRegionId(), command.getDeparturePlaceName()))

				.arrivalPlace(MapperUtility.buildPlaceDto(command.getArrivalPlaceId(),
						command.getArrivalPlaceRegionId(), command.getArrivalPlaceName()))
				.dateTime(command.getDateTime()).endDateTime(command.getEndDateTime())
				.engineType(MapperUtility.buildEngineTypeDto(command.getEngineTypeId(), null))
				.distance(command.getDistance()).workers(command.getWorkers()).description(command.getDescription())
				.build();

		return createJourneyRequestPort.createJourneyRequest(journeyRequest, username);
	}
}
