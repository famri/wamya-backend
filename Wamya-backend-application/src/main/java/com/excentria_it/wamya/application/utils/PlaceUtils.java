package com.excentria_it.wamya.application.utils;

import org.apache.commons.lang3.StringUtils;

import com.excentria_it.wamya.common.exception.InvalidPlaceTypeException;
import com.excentria_it.wamya.domain.PlaceType;

public class PlaceUtils {
	private PlaceUtils() {

	}

	public static PlaceType placeTypeStringToEnum(String placeType) {
		if (StringUtils.isEmpty(placeType)) {
			throw new InvalidPlaceTypeException("Empty or null place type");
		}

		for (PlaceType pt : PlaceType.values()) {
			if (pt.name().equals(placeType.trim().replace("-", "_").toUpperCase())) {
				return pt;
			}
		}
		throw new InvalidPlaceTypeException(String.format("Invalid place type: %s", placeType));

	}
}
