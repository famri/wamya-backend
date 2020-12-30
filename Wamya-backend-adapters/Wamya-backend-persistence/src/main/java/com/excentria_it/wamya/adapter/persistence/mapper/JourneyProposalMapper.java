package com.excentria_it.wamya.adapter.persistence.mapper;

import java.time.LocalDateTime;

import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.VehiculeJpaEntity;
import com.excentria_it.wamya.domain.JourneyProposalDto;
import com.excentria_it.wamya.domain.JourneyProposalDto.VehiculeDto;

public class JourneyProposalMapper {

	public JourneyProposalJpaEntity mapToJpaEntity(Double proposalPrice,
			TransporterJpaEntity transporterAccountJpaEntity, VehiculeJpaEntity vehiculeJpaEntity) {

		return JourneyProposalJpaEntity.builder().price(proposalPrice).vehicule(vehiculeJpaEntity)
				.creationDateTime(LocalDateTime.now()).transporter(transporterAccountJpaEntity).build();
	}

	public JourneyProposalDto mapToDomainEntity(JourneyProposalJpaEntity journeyProposalJpaEntity) {
		if (journeyProposalJpaEntity == null)
			return null;

		VehiculeJpaEntity vehicule = journeyProposalJpaEntity.getVehicule();
		JourneyProposalDto.VehiculeDto vehiculeDto = new VehiculeDto(vehicule.getId(),
				vehicule.getModel().getConstructor().getName(), vehicule.getModel().getName(), vehicule.getPhotoUrl());

		TransporterJpaEntity transporter = journeyProposalJpaEntity.getTransporter();
		JourneyProposalDto.TransporterDto transporterDto = new JourneyProposalDto.TransporterDto(transporter.getId(),
				transporter.getFirstname(), transporter.getPhotoUrl(), transporter.getGlobalRating());

		return JourneyProposalDto.builder().id(journeyProposalJpaEntity.getId())
				.price(journeyProposalJpaEntity.getPrice()).transporterDto(transporterDto).vehiculeDto(vehiculeDto)
				.build();
	}
}
