package com.excentria_it.wamya.adapter.persistence.mapper;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.PlaceJpaEntity;
import com.excentria_it.wamya.domain.JourneyRequestInputOutput;

@Component
public class JourneyRequestMapper {

	public JourneyRequestJpaEntity mapToJpaEntity(JourneyRequestInputOutput journeyRequest,
			PlaceJpaEntity departurePlace, PlaceJpaEntity arrivalPlace, EngineTypeJpaEntity engineType,
			ClientJpaEntity client) {

		if (journeyRequest == null)
			return null;

		return JourneyRequestJpaEntity.builder().id(journeyRequest.getId()).departurePlace(departurePlace)
				.arrivalPlace(arrivalPlace).engineType(engineType).distance(journeyRequest.getDistance())
				.hours(journeyRequest.getHours()).minutes(journeyRequest.getMinutes())
				.dateTime(journeyRequest.getDateTime()).creationDateTime(journeyRequest.getCreationDateTime())
				.workers(journeyRequest.getWorkers()).description(journeyRequest.getDescription()).client(client)
				.build();
	}

	public JourneyRequestInputOutput mapToDomainEntity(JourneyRequestJpaEntity journeyRequestJpaEntity, String locale) {
		if (journeyRequestJpaEntity == null)
			return null;

		return JourneyRequestInputOutput.builder().id(journeyRequestJpaEntity.getId())
				.departurePlace(new JourneyRequestInputOutput.Place(
						journeyRequestJpaEntity.getDeparturePlace().getPlaceId().getId(),
						journeyRequestJpaEntity.getDeparturePlace().getPlaceId().getType(),
						journeyRequestJpaEntity.getDeparturePlace().getLatitude(),
						journeyRequestJpaEntity.getDeparturePlace().getLongitude()))

				.arrivalPlace(new JourneyRequestInputOutput.Place(
						journeyRequestJpaEntity.getArrivalPlace().getPlaceId().getId(),
						journeyRequestJpaEntity.getArrivalPlace().getPlaceId().getType(),
						journeyRequestJpaEntity.getArrivalPlace().getLatitude(),
						journeyRequestJpaEntity.getArrivalPlace().getLongitude()))

				.engineType(new JourneyRequestInputOutput.EngineType(journeyRequestJpaEntity.getEngineType().getId(),
						journeyRequestJpaEntity.getEngineType().getName(locale)))
				.distance(journeyRequestJpaEntity.getDistance()).hours(journeyRequestJpaEntity.getHours())
				.minutes(journeyRequestJpaEntity.getMinutes()).dateTime(journeyRequestJpaEntity.getDateTime())
				.workers(journeyRequestJpaEntity.getWorkers()).description(journeyRequestJpaEntity.getDescription())
				.status(journeyRequestJpaEntity.getStatus().getValue(locale)).build();
	}
}
