package com.excentria_it.wamya.adapter.persistence.mapper;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.adapter.persistence.entity.PlaceJpaEntity;
import com.excentria_it.wamya.domain.PlaceDto;

@Component
public class PlaceMapper {

	public PlaceJpaEntity mapToJpaEntity(PlaceDto placeDto) {
		if (placeDto == null)
			return null;

		return PlaceJpaEntity.builder().id(placeDto.getPlaceId()).regionId(placeDto.getPlaceRegionId())
				.name(placeDto.getPlaceName()).build();
	}

	public PlaceDto mapToDomainEntity(PlaceJpaEntity placeJpaEntity) {
		if (placeJpaEntity == null)
			return null;

		return new PlaceDto(placeJpaEntity.getId(), placeJpaEntity.getRegionId(), placeJpaEntity.getName());
	}
}
