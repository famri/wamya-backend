package com.excentria_it.wamya.adapter.persistence.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.VehiculeJpaEntity;
import com.excentria_it.wamya.application.utils.DocumentUrlResolver;
import com.excentria_it.wamya.common.domain.StatusCode;
import com.excentria_it.wamya.domain.JourneyProposalDto;
import com.excentria_it.wamya.domain.JourneyProposalDto.VehiculeDto;
import com.excentria_it.wamya.domain.TransporterProposalOutput;
import com.excentria_it.wamya.domain.TransporterProposalOutput.JourneyRequestOutput;
import com.excentria_it.wamya.domain.TransporterProposalOutput.JourneyRequestOutput.ClientOutput;
import com.excentria_it.wamya.domain.TransporterProposalOutput.JourneyRequestOutput.EngineTypeDto;
import com.excentria_it.wamya.domain.TransporterProposalOutput.JourneyRequestOutput.PlaceDto;
import com.excentria_it.wamya.domain.TransporterProposalOutput.TransporterVehiculeOutput;

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

	public JourneyProposalDto mapToJourneyProposalDto(JourneyProposalJpaEntity journeyProposalJpaEntity,
			String locale) {
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

	public TransporterProposalOutput mapToTransporterProposalOutput(JourneyProposalJpaEntity jp, String locale) {
		if (jp == null)
			return null;

		VehiculeJpaEntity v = jp.getVehicule();

		Long vehiculeImageId = v.getImage() != null ? v.getImage().getId() : v.getType().getImage().getId();

		String vehiculeImageHash = v.getImage() != null ? v.getImage().getHash() : v.getType().getImage().getHash();

		TransporterProposalOutput.TransporterVehiculeOutput vehicule = TransporterVehiculeOutput.builder().id(v.getId())
				.registrationNumber(v.getRegistration()).circulationDate(v.getCirculationDate())
				.constructorName(v.getModel().getConstructor().getName()).modelName(v.getModel().getName())
				.engineTypeId(v.getType().getId()).engineTypeName(v.getType().getName(locale)).imageId(vehiculeImageId)
				.imageHash(vehiculeImageHash).build();

		JourneyRequestJpaEntity jr = jp.getJourneyRequest();

		TransporterProposalOutput.JourneyRequestOutput.PlaceDto departurePlace = PlaceDto.builder()
				.id(jr.getDeparturePlace().getPlaceId().getId())
				.type(jr.getDeparturePlace().getPlaceId().getType().name()).name(jr.getDeparturePlace().getName(locale))
				.latitude(jr.getDeparturePlace().getLatitude()).longitude(jr.getDeparturePlace().getLongitude())
				.departmentId(jr.getDeparturePlace().getDepartment().getId()).build();

		TransporterProposalOutput.JourneyRequestOutput.PlaceDto arrivalPlace = PlaceDto.builder()
				.id(jr.getArrivalPlace().getPlaceId().getId()).type(jr.getArrivalPlace().getPlaceId().getType().name())
				.name(jr.getArrivalPlace().getName(locale)).latitude(jr.getArrivalPlace().getLatitude())
				.longitude(jr.getArrivalPlace().getLongitude())
				.departmentId(jr.getArrivalPlace().getDepartment().getId()).build();

		TransporterProposalOutput.JourneyRequestOutput.EngineTypeDto engineType = EngineTypeDto.builder()
				.id(jr.getEngineType().getId()).name(jr.getEngineType().getName(locale))
				.code(jr.getEngineType().getCode().name()).build();

		TransporterProposalOutput.JourneyRequestOutput.ClientOutput client = ClientOutput.builder()
				.oauthId(jr.getClient().getOauthId()).firstname(jr.getClient().getFirstname())
				.imageId(jr.getClient().getProfileImage().getId()).imageHash(jr.getClient().getProfileImage().getHash())
				.build();

		TransporterProposalOutput.JourneyRequestOutput journey = JourneyRequestOutput.builder().id(jr.getId())
				.departurePlace(departurePlace).arrivalPlace(arrivalPlace).engineType(engineType)
				.distance(jr.getDistance()).hours(jr.getHours()).minutes(jr.getMinutes()).dateTime(jr.getDateTime())
				.workers(jr.getWorkers()).description(jr.getDescription()).client(client).build();

		return TransporterProposalOutput.builder().id(jp.getId()).price(jp.getPrice())
				.status(jp.getStatus().getValue(locale)).statusCode(jp.getStatus().getCode()).vehicule(vehicule)
				.journey(journey).build();

	}

}
