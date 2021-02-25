package com.excentria_it.wamya.adapter.persistence.mapper;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.adapter.persistence.entity.DepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedPlaceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.PlaceId;
import com.excentria_it.wamya.adapter.persistence.entity.PlaceJpaEntity;
import com.excentria_it.wamya.domain.CreateJourneyRequestDto.PlaceDto;

@Component
public class PlaceMapper {

	public PlaceJpaEntity mapToJpaEntity(PlaceDto placeDto, DepartmentJpaEntity department) {
		if (placeDto == null)
			return null;

		PlaceJpaEntity pje = new PlaceJpaEntity(department, new HashMap<String, LocalizedPlaceJpaEntity>(),
				placeDto.getLatitude(), placeDto.getLongitude());
		pje.setPlaceId(new PlaceId(placeDto.getId(), placeDto.getType()));
		return pje;
	}
}
