package com.excentria_it.wamya.adapter.persistence.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.VehiculeJpaEntity;
import com.excentria_it.wamya.domain.JourneyProposalDto;
import com.excentria_it.wamya.domain.JourneyProposalDto.StatusCode;
import com.excentria_it.wamya.domain.JourneyProposalDto.VehiculeDto;

@Component
public class JourneyProposalMapper {

	public JourneyProposalJpaEntity mapToJpaEntity(Double proposalPrice,
			TransporterJpaEntity transporterAccountJpaEntity, VehiculeJpaEntity vehiculeJpaEntity) {

		return JourneyProposalJpaEntity.builder().price(proposalPrice).vehicule(vehiculeJpaEntity)
				.creationDateTime(LocalDateTime.now()).transporter(transporterAccountJpaEntity).build();
	}

	public JourneyProposalDto mapToDomainEntity(JourneyProposalJpaEntity journeyProposalJpaEntity, String locale) {
		if (journeyProposalJpaEntity == null)
			return null;

		VehiculeJpaEntity vehicule = journeyProposalJpaEntity.getVehicule();
		JourneyProposalDto.VehiculeDto vehiculeDto = new VehiculeDto(vehicule.getId(),
				vehicule.getModel().getConstructor().getName(), vehicule.getModel().getName(), vehicule.getPhotoUrl());

		TransporterJpaEntity transporter = journeyProposalJpaEntity.getTransporter();
		JourneyProposalDto.TransporterDto transporterDto = new JourneyProposalDto.TransporterDto(transporter.getId(),
				transporter.getFirstname(), transporter.getPhotoUrl(), transporter.getGlobalRating());

		return JourneyProposalDto.builder().id(journeyProposalJpaEntity.getId())
				.price(journeyProposalJpaEntity.getPrice()).transporter(transporterDto).vehicule(vehiculeDto)
				.status(new JourneyProposalDto.StatusDto(
						StatusCode.valueOf(journeyProposalJpaEntity.getStatus().getCode().name()),
						journeyProposalJpaEntity.getStatus().getValue(locale)))
				.build();
	}
}
