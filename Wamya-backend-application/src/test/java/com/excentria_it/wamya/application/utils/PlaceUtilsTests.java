package com.excentria_it.wamya.application.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.common.exception.InvalidPlaceTypeException;
import com.excentria_it.wamya.domain.PlaceType;

public class PlaceUtilsTests {

	@Test
	void givenLocalityString_WhenPlaceTypeStringToEnum_ThenReturnLocalityPlaceType() {
		PlaceType locality = PlaceUtils.placeTypeStringToEnum(PlaceType.LOCALITY.name());
		assertEquals(locality.name(), PlaceType.LOCALITY.name());
	}

	@Test
	void givenDelegationString_WhenPlaceTypeStringToEnum_ThenReturnDelegationPlaceType() {
		PlaceType delegation = PlaceUtils.placeTypeStringToEnum(PlaceType.DELEGATION.name());
		assertEquals(delegation.name(), PlaceType.DELEGATION.name());
	}

	@Test
	void givenDepartmentString_WhenPlaceTypeStringToEnum_ThenReturnDepartmentPlaceType() {
		PlaceType department = PlaceUtils.placeTypeStringToEnum(PlaceType.DEPARTMENT.name());
		assertEquals(department.name(), PlaceType.DEPARTMENT.name());
	}

	@Test
	void givenLowerCaseLocalityString_WhenPlaceTypeStringToEnum_ThenReturnLocalityPlaceType() {
		PlaceType locality = PlaceUtils.placeTypeStringToEnum(PlaceType.LOCALITY.name().toLowerCase());
		assertEquals(locality.name(), PlaceType.LOCALITY.name());
	}

	@Test
	void givenLowerCaseDelegationString_WhenPlaceTypeStringToEnum_ThenReturnDelegationPlaceType() {
		PlaceType delegation = PlaceUtils.placeTypeStringToEnum(PlaceType.DELEGATION.name().toLowerCase());
		assertEquals(delegation.name(), PlaceType.DELEGATION.name());
	}

	@Test
	void givenLowerCaseDepartmentString_WhenPlaceTypeStringToEnum_ThenReturnDepartmentPlaceType() {
		PlaceType department = PlaceUtils.placeTypeStringToEnum(PlaceType.DEPARTMENT.name().toLowerCase());
		assertEquals(department.name(), PlaceType.DEPARTMENT.name());
	}

	@Test
	void givenBadPlaceTypeString_WhenPlaceTypeStringToEnum_ThenThrowInvalidPlaceTypeException() {

		assertThrows(InvalidPlaceTypeException.class, () -> PlaceUtils.placeTypeStringToEnum("dep"));
	}
	
	@Test
	void givenEmptyPlaceTypeString_WhenPlaceTypeStringToEnum_ThenThrowInvalidPlaceTypeException() {

		assertThrows(InvalidPlaceTypeException.class, () -> PlaceUtils.placeTypeStringToEnum(""));
	}

}
