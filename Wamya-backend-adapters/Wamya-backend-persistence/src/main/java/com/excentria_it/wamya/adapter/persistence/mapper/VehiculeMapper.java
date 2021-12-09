package com.excentria_it.wamya.adapter.persistence.mapper;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.application.utils.DocumentUrlResolver;
import com.excentria_it.wamya.domain.TransporterVehiculeDto;
import com.excentria_it.wamya.domain.TransporterVehiculeOutput;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class VehiculeMapper {

	private final DocumentUrlResolver documentUrlResolver;

	public TransporterVehiculeDto mapToDomainEntity(TransporterVehiculeOutput transporterVehiculeOutput) {

		String vehiculeImageUrl = (transporterVehiculeOutput.getImage().getId() != null
				&& transporterVehiculeOutput.getImage().getHash() != null)
						? documentUrlResolver.resolveUrl(transporterVehiculeOutput.getImage().getId(),
								transporterVehiculeOutput.getImage().getHash())
						: documentUrlResolver.resolveUrl(transporterVehiculeOutput.getEngineType().getImageId(),
								transporterVehiculeOutput.getEngineType().getImageHash());

		return TransporterVehiculeDto.builder().id(transporterVehiculeOutput.getId())
				.regsitrationNumber(transporterVehiculeOutput.getRegistrationNumber())
				.circulationDate(transporterVehiculeOutput.getCirculationDate())
				.constructorName(transporterVehiculeOutput.getConstructor().getTemporaryName() != null
						? transporterVehiculeOutput.getConstructor().getTemporaryName()
						: transporterVehiculeOutput.getConstructor().getName())
				.modelName(transporterVehiculeOutput.getModel().getTemporaryName() != null
						? transporterVehiculeOutput.getModel().getTemporaryName()
						: transporterVehiculeOutput.getModel().getName())
				.engineTypeId(transporterVehiculeOutput.getEngineType().getId())
				.engineTypeName(transporterVehiculeOutput.getEngineType().getName()).photoUrl(vehiculeImageUrl)

				.build();

	}

}
