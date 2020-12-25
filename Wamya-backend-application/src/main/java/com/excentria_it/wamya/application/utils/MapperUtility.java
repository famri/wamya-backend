package com.excentria_it.wamya.application.utils;

import com.excentria_it.wamya.domain.ClientDto;
import com.excentria_it.wamya.domain.EngineTypeDto;
import com.excentria_it.wamya.domain.PlaceDto;

public class MapperUtility {

	private MapperUtility() {

	}

	public static ClientDto buildClientDto(Long clientId, String clientUsername, String clientFirstname,
			String clientPhotoUrl) {
		return new ClientDto(clientId, clientUsername, clientFirstname, clientPhotoUrl);
	}

	public static PlaceDto buildPlaceDto(String placeId, String placeRegionId, String placeName) {
		return new PlaceDto(placeId, placeRegionId, placeName);
	}

	public static EngineTypeDto buildEngineTypeDto(Long engineTypeId, String engineTypeName) {
		return new EngineTypeDto(engineTypeId, engineTypeName, null);
	}

}
