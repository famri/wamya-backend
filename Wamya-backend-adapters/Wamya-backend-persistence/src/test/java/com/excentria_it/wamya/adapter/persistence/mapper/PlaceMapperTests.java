package com.excentria_it.wamya.adapter.persistence.mapper;

import static com.excentria_it.wamya.test.data.common.DepartmentJpaTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.adapter.persistence.entity.DepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.PlaceJpaEntity;
import com.excentria_it.wamya.domain.CreateJourneyRequestDto.PlaceDto;
import com.excentria_it.wamya.domain.PlaceType;

public class PlaceMapperTests {

	private PlaceMapper placeMapper = new PlaceMapper();

	@Test
	void testMapToJpaEntity() {
		PlaceDto placeDto = new PlaceDto(1L, PlaceType.LOCALITY, new BigDecimal(36.1111), new BigDecimal(10.2222));
		DepartmentJpaEntity department = defaultExistentDepartmentJpaEntity();
		PlaceJpaEntity place = placeMapper.mapToJpaEntity(placeDto, department);

		assertEquals(department, place.getDepartment());

		assertEquals(placeDto.getLatitude(), place.getLatitude());
		assertEquals(placeDto.getLongitude(), place.getLongitude());
		assertEquals(placeDto.getId(), place.getPlaceId().getId());
		assertEquals(placeDto.getType(), place.getPlaceId().getType());
	}

	@Test
	void testMapToJpaEntityFromNullPlaceDto() {
		DepartmentJpaEntity department = defaultExistentDepartmentJpaEntity();
		PlaceJpaEntity place = placeMapper.mapToJpaEntity(null, department);
		assertNull(place);
	}

}
