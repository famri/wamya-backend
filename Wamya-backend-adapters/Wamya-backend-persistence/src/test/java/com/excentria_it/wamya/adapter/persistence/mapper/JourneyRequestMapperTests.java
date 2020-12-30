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
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.domain.CreateJourneyRequestDto;

public class JourneyRequestMapperTests {

	private JourneyRequestMapper journeyRequestMapper = new JourneyRequestMapper();

	@Test
	void testMapToJpaEntity() {
		CreateJourneyRequestDto journeyRequest = defaultCreateJourneyRequestDto();
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
		assertEquals(journeyRequest.getEndDateTime(), journeyRequestJpaEntity.getEndDateTime());

		assertEquals(journeyRequest.getDistance(), journeyRequestJpaEntity.getDistance());

		assertEquals(journeyRequest.getWorkers(), journeyRequestJpaEntity.getWorkers());

		assertEquals(journeyRequest.getDescription(), journeyRequestJpaEntity.getDescription());

		assertEquals(clientJpaEntity, journeyRequestJpaEntity.getClient());

	}

	@Test
	void testMapToJpaEntityFromNullDomainEntity() {
		CreateJourneyRequestDto journeyRequest = null;
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
		CreateJourneyRequestDto journeyRequest = journeyRequestMapper.mapToDomainEntity(journeyRequestJpaEntity, "en");

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

	}

	@Test
	void testMapToDoaminEntityFromNullJpaEntity() {
		JourneyRequestJpaEntity journeyRequestJpaEntity = null;
		CreateJourneyRequestDto journeyRequest = journeyRequestMapper.mapToDomainEntity(journeyRequestJpaEntity, "en");

		assertNull(journeyRequest);

	}
}
