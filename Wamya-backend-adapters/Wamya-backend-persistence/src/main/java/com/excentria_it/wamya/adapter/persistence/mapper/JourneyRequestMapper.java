package com.excentria_it.wamya.adapter.persistence.mapper;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.PlaceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.application.utils.MapperUtility;
import com.excentria_it.wamya.domain.JourneyRequest;

@Component
public class JourneyRequestMapper {
	public JourneyRequestJpaEntity mapToJpaEntity(JourneyRequest journeyRequest, PlaceJpaEntity departurePlace,
			PlaceJpaEntity arrivalPlace, EngineTypeJpaEntity engineType, UserAccountJpaEntity client,
			Set<JourneyProposalJpaEntity> proposals) {

		if (journeyRequest == null)
			return null;

		return JourneyRequestJpaEntity.builder().id(journeyRequest.getId()).departurePlace(departurePlace)
				.arrivalPlace(arrivalPlace).engineType(engineType).distance(journeyRequest.getDistance())
				.dateTime(journeyRequest.getDateTime()).endDateTime(journeyRequest.getEndDateTime())
				.workers(journeyRequest.getWorkers()).description(journeyRequest.getDescription()).client(client)
				.proposals(proposals).build();
	}

	public JourneyRequest mapToDomainEntity(JourneyRequestJpaEntity journeyRequestJpaEntity, String locale) {
		if (journeyRequestJpaEntity == null)
			return null;

		return JourneyRequest.builder().id(journeyRequestJpaEntity.getId())
				.departurePlace(MapperUtility.buildPlaceDto(journeyRequestJpaEntity.getDeparturePlace().getId(),
						journeyRequestJpaEntity.getDeparturePlace().getRegionId(),
						journeyRequestJpaEntity.getDeparturePlace().getName()))
				.arrivalPlace(MapperUtility.buildPlaceDto(journeyRequestJpaEntity.getArrivalPlace().getId(),
						journeyRequestJpaEntity.getArrivalPlace().getRegionId(),
						journeyRequestJpaEntity.getArrivalPlace().getName()))
				.engineType(MapperUtility.buildEngineTypeDto(journeyRequestJpaEntity.getEngineType().getId(),
						journeyRequestJpaEntity.getEngineType().getName(locale)))
				.distance(journeyRequestJpaEntity.getDistance()).dateTime(journeyRequestJpaEntity.getDateTime())
				.endDateTime(journeyRequestJpaEntity.getEndDateTime()).workers(journeyRequestJpaEntity.getWorkers())
				.description(journeyRequestJpaEntity.getDescription())
				.client(MapperUtility.buildClientDto(journeyRequestJpaEntity.getClient().getId(),
						journeyRequestJpaEntity.getClient().getEmail(),
						journeyRequestJpaEntity.getClient().getFirstname(),
						journeyRequestJpaEntity.getClient().getPhotoUrl()))
				.build();
	}
}
