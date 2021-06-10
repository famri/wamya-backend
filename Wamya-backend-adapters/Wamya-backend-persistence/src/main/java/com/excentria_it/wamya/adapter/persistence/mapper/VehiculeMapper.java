package com.excentria_it.wamya.adapter.persistence.mapper;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.adapter.persistence.entity.VehiculeJpaEntity;
import com.excentria_it.wamya.application.utils.DocumentUrlResolver;
import com.excentria_it.wamya.domain.JourneyProposalDto.VehiculeDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class VehiculeMapper {

	private final DocumentUrlResolver documentUrlResolver;

	public VehiculeDto mapToDomainEntity(VehiculeJpaEntity vehiculeJpaEntity) {

		String vehiculeImageUrl = (vehiculeJpaEntity.getImage() != null)
				? documentUrlResolver.resolveUrl(vehiculeJpaEntity.getImage().getId(),
						vehiculeJpaEntity.getImage().getHash())
				: documentUrlResolver.resolveUrl(vehiculeJpaEntity.getType().getImage().getId(),
						vehiculeJpaEntity.getType().getImage().getHash());

		if (vehiculeJpaEntity.getTemporaryModel() != null) {
			return new VehiculeDto(vehiculeJpaEntity.getId(),
					vehiculeJpaEntity.getTemporaryModel().getConstructorName(),
					vehiculeJpaEntity.getTemporaryModel().getModelName(), vehiculeImageUrl);

		}

		return new VehiculeDto(vehiculeJpaEntity.getId(), vehiculeJpaEntity.getModel().getConstructor().getName(),
				vehiculeJpaEntity.getModel().getName(), vehiculeImageUrl);

	}
}
