package com.excentria_it.wamya.adapter.persistence.mapper;

import static com.excentria_it.wamya.test.data.common.JourneyRequestJpaTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.adapter.persistence.entity.PlaceJpaEntity;
import com.excentria_it.wamya.domain.PlaceDto;

public class PlaceMapperTests {

	private PlaceMapper placeMapper = new PlaceMapper();

	@Test
	void testMapToJpaEntity() {

		PlaceDto placeDto = new PlaceDto("placeId", "placeRegionId", "placeName");
		PlaceJpaEntity placeJpaEntity = placeMapper.mapToJpaEntity(placeDto);

		assertEquals(placeDto.getPlaceId(), placeJpaEntity.getId());
		assertEquals(placeDto.getPlaceRegionId(), placeJpaEntity.getRegionId());
		assertEquals(placeDto.getPlaceName(), placeJpaEntity.getName());
	}

	@Test
	void testMapToJpaEntityFromNullDomainEntity() {

		PlaceDto placeDto = null;
		PlaceJpaEntity placeJpaEntity = placeMapper.mapToJpaEntity(placeDto);

		assertNull(placeJpaEntity);

	}

	@Test
	void testMapToDomainEntity() {
		PlaceJpaEntity placeJpaEntity = defaultDeparturePlaceJpaEntity();
		PlaceDto placeDto = placeMapper.mapToDomainEntity(placeJpaEntity);

		assertEquals(placeJpaEntity.getId(), placeDto.getPlaceId());
		assertEquals(placeJpaEntity.getRegionId(), placeDto.getPlaceRegionId());
		assertEquals(placeJpaEntity.getName(), placeDto.getPlaceName());

	}

	@Test
	void testMapToDomainEntityFromNullJpaEntity() {
		PlaceJpaEntity placeJpaEntity = null;
		PlaceDto placeDto = placeMapper.mapToDomainEntity(placeJpaEntity);

		assertNull(placeDto);

	}
}
