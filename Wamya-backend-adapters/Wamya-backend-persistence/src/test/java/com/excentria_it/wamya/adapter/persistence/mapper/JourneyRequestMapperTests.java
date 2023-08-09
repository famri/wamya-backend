package com.excentria_it.wamya.adapter.persistence.mapper;

import static com.excentria_it.wamya.test.data.common.JourneyRequestJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.JourneyRequestTestData.*;
import static com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.PlaceJpaEntity;
import com.excentria_it.wamya.domain.JourneyRequestInputOutput;

public class JourneyRequestMapperTests {

	private JourneyRequestMapper journeyRequestMapper = new JourneyRequestMapper();

	@Test
	void testMapToJpaEntity() {
		JourneyRequestInputOutput journeyRequest = defaultJourneyRequestInputOutputBuilder().build();
		PlaceJpaEntity departurePlaceJpaEntity = defaultDeparturePlaceJpaEntity();
		PlaceJpaEntity arrivalPlaceJpaEntity = defaultArrivalPlaceJpaEntity();
		EngineTypeJpaEntity engineTypeJpaEntity = defaultEngineTypeJpaEntity();

		ClientJpaEntity clientJpaEntity = defaultExistentClientJpaEntity();

		JourneyRequestJpaEntity journeyRequestJpaEntity = journeyRequestMapper.mapToJpaEntity(journeyRequest,
				departurePlaceJpaEntity, arrivalPlaceJpaEntity, engineTypeJpaEntity, clientJpaEntity);

		assertEquals(journeyRequest.getId(), journeyRequestJpaEntity.getId());

		assertEquals(arrivalPlaceJpaEntity, journeyRequestJpaEntity.getArrivalPlace());
		assertEquals(departurePlaceJpaEntity, journeyRequestJpaEntity.getDeparturePlace());

		assertEquals(engineTypeJpaEntity, journeyRequestJpaEntity.getEngineType());

		assertEquals(journeyRequest.getDateTime(), journeyRequestJpaEntity.getDateTime());

		assertEquals(journeyRequest.getDistance(), journeyRequestJpaEntity.getDistance());

		assertEquals(journeyRequest.getWorkers(), journeyRequestJpaEntity.getWorkers());

		assertEquals(journeyRequest.getDescription(), journeyRequestJpaEntity.getDescription());

		assertEquals(clientJpaEntity, journeyRequestJpaEntity.getClient());

	}

	@Test
	void testMapToJpaEntityFromNullDomainEntity() {
		JourneyRequestInputOutput journeyRequest = null;
		PlaceJpaEntity departurePlaceJpaEntity = defaultDeparturePlaceJpaEntity();
		PlaceJpaEntity arrivalPlaceJpaEntity = defaultArrivalPlaceJpaEntity();
		EngineTypeJpaEntity engineTypeJpaEntity = defaultEngineTypeJpaEntity();

		ClientJpaEntity clientJpaEntity = defaultExistentClientJpaEntity();

		JourneyRequestJpaEntity journeyRequestJpaEntity = journeyRequestMapper.mapToJpaEntity(journeyRequest,
				departurePlaceJpaEntity, arrivalPlaceJpaEntity, engineTypeJpaEntity, clientJpaEntity);

		assertNull(journeyRequestJpaEntity);
	}

	@Test
	void testMapToDoaminEntity() {

		JourneyRequestJpaEntity journeyRequestJpaEntity = defaultExistentJourneyRequestJpaEntity();
		JourneyRequestInputOutput journeyRequest = journeyRequestMapper.mapToDomainEntity(journeyRequestJpaEntity,
				"en_US");

		assertEquals(journeyRequestJpaEntity.getId(), journeyRequest.getId());

		assertEquals(journeyRequestJpaEntity.getDeparturePlace().getPlaceId().getId(),
				journeyRequest.getDeparturePlace().getId());

		assertEquals(journeyRequestJpaEntity.getDeparturePlace().getPlaceId().getType(),
				journeyRequest.getDeparturePlace().getType());

		assertEquals(journeyRequestJpaEntity.getArrivalPlace().getPlaceId().getId(),
				journeyRequest.getArrivalPlace().getId());
		assertEquals(journeyRequestJpaEntity.getArrivalPlace().getPlaceId().getType(),
				journeyRequest.getArrivalPlace().getType());

		assertEquals(journeyRequestJpaEntity.getDistance(), journeyRequest.getDistance());

		assertEquals(journeyRequestJpaEntity.getDateTime(), journeyRequest.getDateTime());

		assertEquals(journeyRequestJpaEntity.getWorkers(), journeyRequest.getWorkers());

		assertEquals(journeyRequestJpaEntity.getDescription(), journeyRequest.getDescription());

	}

	@Test
	void testMapToDoaminEntityFromNullJpaEntity() {
		JourneyRequestJpaEntity journeyRequestJpaEntity = null;
		JourneyRequestInputOutput journeyRequest = journeyRequestMapper.mapToDomainEntity(journeyRequestJpaEntity,
				"en");

		assertNull(journeyRequest);

	}
}
