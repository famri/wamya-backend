package com.excentria_it.wamya.adapter.persistence.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.excentria_it.wamya.adapter.persistence.entity.GeoPlaceJpaEntity;
import com.excentria_it.wamya.domain.DepartmentDto;
import com.excentria_it.wamya.domain.GeoPlaceDto;

@Component
public class GeoPlaceMapper {
	public GeoPlaceDto mapToDomainEntity(GeoPlaceJpaEntity geoPlaceJpaEntity, String locale) {

		return new GeoPlaceDto(geoPlaceJpaEntity.getId(), geoPlaceJpaEntity.getName(), geoPlaceJpaEntity.getLatitude(),
				geoPlaceJpaEntity.getLongitude(), new DepartmentDto(geoPlaceJpaEntity.getDepartment().getId(),
						geoPlaceJpaEntity.getDepartment().getName(locale)));
	}

	public List<GeoPlaceDto> mapToDomainEntities(List<GeoPlaceJpaEntity> geoPlaceJpaEntities, String locale) {
		if (CollectionUtils.isEmpty(geoPlaceJpaEntities)) {
			return Collections.emptyList();
		}
		return geoPlaceJpaEntities.stream()
				.map(g -> new GeoPlaceDto(g.getId(), g.getName(), g.getLatitude(), g.getLongitude(),
						new DepartmentDto(g.getDepartment().getId(), g.getDepartment().getName(locale))))
				.collect(Collectors.toList());
	}
}
