package com.excentria_it.wamya.adapter.persistence.mapper;

import static com.excentria_it.wamya.test.data.common.JourneyRequestJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.JourneyRequestTestData.*;
import static com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.PlaceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.domain.JourneyRequest;

public class JourneyRequestMapperTests {

	private JourneyRequestMapper journeyRequestMapper = new JourneyRequestMapper();

	@Test
	void testMapToJpaEntity() {
		JourneyRequest journeyRequest = defaultJourneyRequest();
		PlaceJpaEntity departurePlaceJpaEntity = defaultDeparturePlaceJpaEntity();
		PlaceJpaEntity arrivalPlaceJpaEntity = defaultArrivalPlaceJpaEntity();
		EngineTypeJpaEntity engineTypeJpaEntity = defaultEngineTypeJpaEntity();

		UserAccountJpaEntity userAccountJpaEntity = defaultNewNotTransporterUserAccountJpaEntity();

		JourneyRequestJpaEntity journeyRequestJpaEntity = journeyRequestMapper.mapToJpaEntity(journeyRequest,
				departurePlaceJpaEntity, arrivalPlaceJpaEntity, engineTypeJpaEntity, userAccountJpaEntity, null);

		assertEquals(journeyRequest.getId(), journeyRequestJpaEntity.getId());

		assertEquals(arrivalPlaceJpaEntity, journeyRequestJpaEntity.getArrivalPlace());
		assertEquals(departurePlaceJpaEntity, journeyRequestJpaEntity.getDeparturePlace());

		assertEquals(engineTypeJpaEntity, journeyRequestJpaEntity.getEngineType());

		assertEquals(journeyRequest.getDateTime(), journeyRequestJpaEntity.getDateTime());
		assertEquals(journeyRequest.getEndDateTime(), journeyRequestJpaEntity.getEndDateTime());

		assertEquals(journeyRequest.getDistance(), journeyRequestJpaEntity.getDistance());

		assertEquals(journeyRequest.getWorkers(), journeyRequestJpaEntity.getWorkers());

		assertEquals(journeyRequest.getDescription(), journeyRequestJpaEntity.getDescription());

		assertEquals(userAccountJpaEntity, journeyRequestJpaEntity.getClient());

	}

	@Test
	void testMapToJpaEntityFromNullDomainEntity() {
		JourneyRequest journeyRequest = null;
		PlaceJpaEntity departurePlaceJpaEntity = defaultDeparturePlaceJpaEntity();
		PlaceJpaEntity arrivalPlaceJpaEntity = defaultArrivalPlaceJpaEntity();
		EngineTypeJpaEntity engineTypeJpaEntity = defaultEngineTypeJpaEntity();

		UserAccountJpaEntity userAccountJpaEntity = defaultNewNotTransporterUserAccountJpaEntity();

		JourneyRequestJpaEntity journeyRequestJpaEntity = journeyRequestMapper.mapToJpaEntity(journeyRequest,
				departurePlaceJpaEntity, arrivalPlaceJpaEntity, engineTypeJpaEntity, userAccountJpaEntity, null);

		assertNull(journeyRequestJpaEntity);
	}

	@Test
	void testMapToDoaminEntity() {
		JourneyRequestJpaEntity journeyRequestJpaEntity = defaultNewJourneyRequestJpaEntity();
		JourneyRequest journeyRequest = journeyRequestMapper.mapToDomainEntity(journeyRequestJpaEntity, "en");

		assertEquals(journeyRequestJpaEntity.getId(), journeyRequest.getId());

		assertEquals(journeyRequestJpaEntity.getDeparturePlace().getId(),
				journeyRequest.getDeparturePlace().getPlaceId());
		assertEquals(journeyRequestJpaEntity.getDeparturePlace().getRegionId(),
				journeyRequest.getDeparturePlace().getPlaceRegionId());
		assertEquals(journeyRequestJpaEntity.getDeparturePlace().getName(),
				journeyRequest.getDeparturePlace().getPlaceName());

		assertEquals(journeyRequestJpaEntity.getArrivalPlace().getId(), journeyRequest.getArrivalPlace().getPlaceId());
		assertEquals(journeyRequestJpaEntity.getArrivalPlace().getRegionId(),
				journeyRequest.getArrivalPlace().getPlaceRegionId());
		assertEquals(journeyRequestJpaEntity.getArrivalPlace().getName(),
				journeyRequest.getArrivalPlace().getPlaceName());

		assertEquals(journeyRequestJpaEntity.getDistance(), journeyRequest.getDistance());

		assertEquals(journeyRequestJpaEntity.getDateTime(), journeyRequest.getDateTime());
		assertEquals(journeyRequestJpaEntity.getEndDateTime(), journeyRequest.getEndDateTime());

		assertEquals(journeyRequestJpaEntity.getWorkers(), journeyRequest.getWorkers());

		assertEquals(journeyRequestJpaEntity.getDescription(), journeyRequest.getDescription());

		assertEquals(journeyRequestJpaEntity.getClient().getId(), journeyRequest.getClient().getId());
		assertEquals(journeyRequestJpaEntity.getClient().getFirstname(), journeyRequest.getClient().getFirstname());
		assertEquals(journeyRequestJpaEntity.getClient().getEmail(), journeyRequest.getClient().getUsername());
		assertEquals(journeyRequestJpaEntity.getClient().getPhotoUrl(), journeyRequest.getClient().getPhotoUrl());

	}

	@Test
	void testMapToDoaminEntityFromNullJpaEntity() {
		JourneyRequestJpaEntity journeyRequestJpaEntity = null;
		JourneyRequest journeyRequest = journeyRequestMapper.mapToDomainEntity(journeyRequestJpaEntity, "en");

		assertNull(journeyRequest);

	}
}
