package com.excentria_it.wamya.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class GeoPlaceDtoTests {
	@Test
	void testBuilder() {
		DepartmentDto department = new DepartmentDto(1L, "Department");
		GeoPlaceDto geoPlace = new GeoPlaceDto.Builder().withId(1L).withName("geoPlace")
				.withLatitude(new BigDecimal(36.7777)).withLongitude(new BigDecimal(10.7777)).withDepartment(department)
				.build();
		assertEquals(1L, geoPlace.getId());
		assertEquals(new BigDecimal(36.7777), geoPlace.getLatitude());
		assertEquals(new BigDecimal(10.7777), geoPlace.getLongitude());
		assertEquals(department, geoPlace.getDepartment());
	}
}
