package com.excentria_it.wamya.adapter.persistence.mapper;

import static com.excentria_it.wamya.test.data.common.GeoPlaceJpaTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.adapter.persistence.entity.GeoPlaceJpaEntity;
import com.excentria_it.wamya.domain.GeoPlaceDto;

public class GeoPlaceMapperTests {
	private GeoPlaceMapper geoPlaceMapper = new GeoPlaceMapper();

	@Test
	void testMapToDomainEntity() {
		// given
		GeoPlaceJpaEntity geoPlaceJpaEntity = defaultGeoPlaceJpaEntity();

		// when
		GeoPlaceDto geoPlaceDto = geoPlaceMapper.mapToDomainEntity(geoPlaceJpaEntity, "fr_FR");
		// then
		assertEquals(geoPlaceJpaEntity.getId(), geoPlaceDto.getId());
		assertEquals(geoPlaceJpaEntity.getName(), geoPlaceDto.getName());
		assertEquals(geoPlaceJpaEntity.getLatitude(), geoPlaceDto.getLatitude());
		assertEquals(geoPlaceJpaEntity.getLongitude(), geoPlaceDto.getLongitude());
		assertEquals(geoPlaceJpaEntity.getDepartment().getId(), geoPlaceDto.getDepartment().getId());
		assertEquals(geoPlaceJpaEntity.getDepartment().getName("fr_FR"), geoPlaceDto.getDepartment().getName());

	}

	@Test
	void testMapToDomainEntities() {
		// given
		GeoPlaceJpaEntity geoPlaceJpaEntity = defaultGeoPlaceJpaEntity();
		List<GeoPlaceJpaEntity> geoPlaceJpaEntitySet = List.of(geoPlaceJpaEntity);
		// when
		List<GeoPlaceDto> geoPlaceDtoSet = geoPlaceMapper.mapToDomainEntities(geoPlaceJpaEntitySet, "fr_FR");
		// then

		assertTrue(geoPlaceDtoSet.stream().map(gd -> gd.getId()).collect(Collectors.toSet())
				.containsAll(geoPlaceJpaEntitySet.stream().map(ge -> ge.getId()).collect(Collectors.toSet())));
		assertTrue(geoPlaceDtoSet.stream().map(gd -> gd.getName()).collect(Collectors.toSet())
				.containsAll(geoPlaceJpaEntitySet.stream().map(ge -> ge.getName()).collect(Collectors.toSet())));
		assertTrue(geoPlaceDtoSet.stream().map(gd -> gd.getDepartment().getId()).collect(Collectors.toSet())
				.containsAll(geoPlaceJpaEntitySet.stream().map(ge -> ge.getDepartment().getId())
						.collect(Collectors.toSet())));
		assertTrue(

				geoPlaceDtoSet.stream().map(gd -> gd.getDepartment().getName()).collect(Collectors.toSet())
						.containsAll(geoPlaceJpaEntitySet.stream().map(ge -> ge.getDepartment().getName("fr_FR"))
								.collect(Collectors.toSet())));
		assertTrue(

				geoPlaceDtoSet.stream().map(gd -> gd.getLatitude()).collect(Collectors.toSet()).containsAll(
						geoPlaceJpaEntitySet.stream().map(ge -> ge.getLatitude()).collect(Collectors.toSet())));

		assertTrue(

				geoPlaceDtoSet.stream().map(gd -> gd.getLongitude()).collect(Collectors.toSet()).containsAll(
						geoPlaceJpaEntitySet.stream().map(ge -> ge.getLongitude()).collect(Collectors.toSet())));
	}

	@Test
	void testMapToDomainEntitiesFromEmptySet() {
		// given

		// when
		List<GeoPlaceDto> geoPlaceDtoSet = geoPlaceMapper.mapToDomainEntities(Collections.emptyList(), "fr_FR");
		// then

		assertTrue(geoPlaceDtoSet.isEmpty());
	}

	@Test
	void testMapToDomainEntitiesFromNullSet() {
		// given

		// when
		List<GeoPlaceDto> geoPlaceDtoSet = geoPlaceMapper.mapToDomainEntities(null, "fr_FR");
		// then

		assertTrue(geoPlaceDtoSet.isEmpty());
	}
}
