package com.excentria_it.wamya.application.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.domain.ClientDto;
import com.excentria_it.wamya.domain.EngineTypeDto;
import com.excentria_it.wamya.domain.PlaceDto;

public class MapperUtilityTests {

	@Test
	void testBuildClientDto() {
		ClientDto clientDto = MapperUtility.buildClientDto(1L, "someUsername", "someFirstname", "someUrl");
		assertEquals(1L, clientDto.getId());
		assertEquals("someUsername", clientDto.getUsername());
		assertEquals("someFirstname", clientDto.getFirstname());
		assertEquals("someUrl", clientDto.getPhotoUrl());
	}

	@Test
	void testBuildPlaceDto() {
		PlaceDto placeDto = MapperUtility.buildPlaceDto("placeId", "placeRegionId", "placeName");
		assertEquals("placeId", placeDto.getPlaceId());
		assertEquals("placeRegionId", placeDto.getPlaceRegionId());
		assertEquals("placeName", placeDto.getPlaceName());
	}

	@Test
	void testBuildEngineTypeDto() {
		EngineTypeDto engineTypeDto = MapperUtility.buildEngineTypeDto(1L, "engineTypeName");
		assertEquals(1L, engineTypeDto.getId());
		assertEquals("engineTypeName", engineTypeDto.getName());
	}
}
