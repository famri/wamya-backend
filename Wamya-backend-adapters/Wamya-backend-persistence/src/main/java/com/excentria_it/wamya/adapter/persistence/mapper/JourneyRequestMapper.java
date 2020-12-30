package com.excentria_it.wamya.adapter.persistence.mapper;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.PlaceJpaEntity;
import com.excentria_it.wamya.domain.CreateJourneyRequestDto;

@Component
public class JourneyRequestMapper {

	public JourneyRequestJpaEntity mapToJpaEntity(CreateJourneyRequestDto journeyRequest, PlaceJpaEntity departurePlace,
			PlaceJpaEntity arrivalPlace, EngineTypeJpaEntity engineType, ClientJpaEntity client) {

		if (journeyRequest == null)
			return null;

		return JourneyRequestJpaEntity.builder().id(journeyRequest.getId()).departurePlace(departurePlace)
				.arrivalPlace(arrivalPlace).engineType(engineType).distance(journeyRequest.getDistance())
				.dateTime(journeyRequest.getDateTime()).endDateTime(journeyRequest.getEndDateTime())
				.workers(journeyRequest.getWorkers()).description(journeyRequest.getDescription()).client(client)
				.build();
	}

	public CreateJourneyRequestDto mapToDomainEntity(JourneyRequestJpaEntity journeyRequestJpaEntity, String locale) {
		if (journeyRequestJpaEntity == null)
			return null;

		return CreateJourneyRequestDto.builder().id(journeyRequestJpaEntity.getId())
				.departurePlace(
						new CreateJourneyRequestDto.PlaceDto(journeyRequestJpaEntity.getDeparturePlace().getId(),
								journeyRequestJpaEntity.getDeparturePlace().getRegionId(),
								journeyRequestJpaEntity.getDeparturePlace().getName()))
				.arrivalPlace(new CreateJourneyRequestDto.PlaceDto(journeyRequestJpaEntity.getArrivalPlace().getId(),
						journeyRequestJpaEntity.getArrivalPlace().getRegionId(),
						journeyRequestJpaEntity.getArrivalPlace().getName()))
				.engineType(new CreateJourneyRequestDto.EngineTypeDto(journeyRequestJpaEntity.getEngineType().getId(),
						journeyRequestJpaEntity.getEngineType().getName(locale)))
				.distance(journeyRequestJpaEntity.getDistance()).dateTime(journeyRequestJpaEntity.getDateTime())
				.endDateTime(journeyRequestJpaEntity.getEndDateTime()).workers(journeyRequestJpaEntity.getWorkers())
				.description(journeyRequestJpaEntity.getDescription())

				.build();
	}
}
