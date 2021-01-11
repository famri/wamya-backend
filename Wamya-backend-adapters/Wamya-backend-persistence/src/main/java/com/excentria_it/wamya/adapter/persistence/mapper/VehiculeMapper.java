package com.excentria_it.wamya.adapter.persistence.mapper;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.adapter.persistence.entity.VehiculeJpaEntity;
import com.excentria_it.wamya.domain.JourneyProposalDto.VehiculeDto;

@Component
public class VehiculeMapper {

	public VehiculeDto mapToDomainEntity(VehiculeJpaEntity vehiculeJpaEntity) {
		if(vehiculeJpaEntity.getTemporaryModel()!=null) {
			return new VehiculeDto(vehiculeJpaEntity.getId(), vehiculeJpaEntity.getTemporaryModel().getConstructorName(),
					vehiculeJpaEntity.getTemporaryModel().getModelName(), vehiculeJpaEntity.getPhotoUrl());

		}
		
		return new VehiculeDto(vehiculeJpaEntity.getId(), vehiculeJpaEntity.getModel().getConstructor().getName(),
				vehiculeJpaEntity.getModel().getName(), vehiculeJpaEntity.getPhotoUrl());

	}
}
