package com.excentria_it.wamya.adapter.persistence.mapper;

import static com.excentria_it.wamya.test.data.common.DepartmentJpaTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.adapter.persistence.entity.DepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.PlaceJpaEntity;
import com.excentria_it.wamya.domain.JourneyRequestInputOutput.Place;
import com.excentria_it.wamya.domain.PlaceType;

public class PlaceMapperTests {

	private PlaceMapper placeMapper = new PlaceMapper();

	@Test
	void testMapToJpaEntity() {
		Place place = new Place(1L, PlaceType.LOCALITY, new BigDecimal(36.1111), new BigDecimal(10.2222));
		DepartmentJpaEntity department = defaultExistentDepartmentJpaEntity();
		PlaceJpaEntity placeEntity = placeMapper.mapToJpaEntity(place, department);

		assertEquals(department, placeEntity.getDepartment());

		assertEquals(place.getLatitude(), placeEntity.getLatitude());
		assertEquals(place.getLongitude(), placeEntity.getLongitude());
		assertEquals(place.getId(), placeEntity.getPlaceId().getId());
		assertEquals(place.getType(), placeEntity.getPlaceId().getType());
	}

	@Test
	void testMapToJpaEntityFromNullPlaceDto() {
		DepartmentJpaEntity department = defaultExistentDepartmentJpaEntity();
		PlaceJpaEntity place = placeMapper.mapToJpaEntity(null, department);
		assertNull(place);
	}

}
