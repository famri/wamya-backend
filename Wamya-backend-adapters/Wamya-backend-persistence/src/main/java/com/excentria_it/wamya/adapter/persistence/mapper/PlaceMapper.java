package com.excentria_it.wamya.adapter.persistence.mapper;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.adapter.persistence.entity.DepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedPlaceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.PlaceId;
import com.excentria_it.wamya.adapter.persistence.entity.PlaceJpaEntity;
import com.excentria_it.wamya.domain.JourneyRequestInputOutput.Place;

@Component
public class PlaceMapper {

	public PlaceJpaEntity mapToJpaEntity(Place place, DepartmentJpaEntity department) {
		if (place == null)
			return null;

		PlaceJpaEntity pje = new PlaceJpaEntity(department, new HashMap<String, LocalizedPlaceJpaEntity>(),
				place.getLatitude(), place.getLongitude());
		pje.setPlaceId(new PlaceId(place.getId(), place.getType()));
		return pje;
	}
}
