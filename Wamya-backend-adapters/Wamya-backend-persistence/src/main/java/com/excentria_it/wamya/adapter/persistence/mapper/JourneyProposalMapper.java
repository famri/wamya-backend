package com.excentria_it.wamya.adapter.persistence.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.VehiculeJpaEntity;
import com.excentria_it.wamya.application.utils.DocumentUrlResolver;
import com.excentria_it.wamya.common.domain.StatusCode;
import com.excentria_it.wamya.domain.JourneyProposalDto;
import com.excentria_it.wamya.domain.JourneyProposalDto.VehiculeDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JourneyProposalMapper {

	private final DocumentUrlResolver documentUrlResolver;

	public JourneyProposalJpaEntity mapToJpaEntity(Double proposalPrice,
			TransporterJpaEntity transporterAccountJpaEntity, VehiculeJpaEntity vehiculeJpaEntity) {

		return JourneyProposalJpaEntity.builder().price(proposalPrice).vehicule(vehiculeJpaEntity)
				.creationDateTime(LocalDateTime.now()).transporter(transporterAccountJpaEntity).build();
	}

	public JourneyProposalDto mapToDomainEntity(JourneyProposalJpaEntity journeyProposalJpaEntity, String locale) {
		if (journeyProposalJpaEntity == null)
			return null;

		VehiculeJpaEntity vehicule = journeyProposalJpaEntity.getVehicule();

		String vehiculeImageUrl = (vehicule.getImage() != null)
				? documentUrlResolver.resolveUrl(vehicule.getImage().getId(), vehicule.getImage().getHash())
				: documentUrlResolver.resolveUrl(vehicule.getType().getImage().getId(),
						vehicule.getType().getImage().getHash());

		JourneyProposalDto.VehiculeDto vehiculeDto = new VehiculeDto(vehicule.getId(),
				vehicule.getModel().getConstructor().getName(), vehicule.getModel().getName(), vehiculeImageUrl);

		TransporterJpaEntity transporter = journeyProposalJpaEntity.getTransporter();
		JourneyProposalDto.TransporterDto transporterDto = new JourneyProposalDto.TransporterDto(
				transporter.getOauthId(), transporter.getFirstname(), documentUrlResolver
						.resolveUrl(transporter.getProfileImage().getId(), transporter.getProfileImage().getHash()),
				transporter.getGlobalRating());

		return JourneyProposalDto.builder().id(journeyProposalJpaEntity.getId())
				.price(journeyProposalJpaEntity.getPrice()).transporter(transporterDto).vehicule(vehiculeDto)
				.status(new JourneyProposalDto.StatusDto(
						StatusCode.valueOf(journeyProposalJpaEntity.getStatus().getCode().name()),
						journeyProposalJpaEntity.getStatus().getValue(locale)))
				.build();
	}
}
