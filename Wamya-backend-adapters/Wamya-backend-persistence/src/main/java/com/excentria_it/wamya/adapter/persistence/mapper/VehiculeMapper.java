package com.excentria_it.wamya.adapter.persistence.mapper;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.application.utils.DocumentUrlResolver;
import com.excentria_it.wamya.domain.TransporterVehicleDto;
import com.excentria_it.wamya.domain.TransporterVehicleOutput;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class VehiculeMapper {

	private final DocumentUrlResolver documentUrlResolver;

	public TransporterVehicleDto mapToDomainEntity(TransporterVehicleOutput transporterVehicleOutput) {

		String vehiculeImageUrl = (transporterVehicleOutput.getImage().getId() != null
				&& transporterVehicleOutput.getImage().getHash() != null)
						? documentUrlResolver.resolveUrl(transporterVehicleOutput.getImage().getId(),
								transporterVehicleOutput.getImage().getHash())
						: documentUrlResolver.resolveUrl(transporterVehicleOutput.getEngineType().getImageId(),
								transporterVehicleOutput.getEngineType().getImageHash());

		return TransporterVehicleDto.builder().id(transporterVehicleOutput.getId())
				.registrationNumber(transporterVehicleOutput.getRegistrationNumber())
				.circulationDate(transporterVehicleOutput.getCirculationDate())
				.constructorName(transporterVehicleOutput.getConstructor().getTemporaryName() != null
						? transporterVehicleOutput.getConstructor().getTemporaryName()
						: transporterVehicleOutput.getConstructor().getName())
				.modelName(transporterVehicleOutput.getModel().getTemporaryName() != null
						? transporterVehicleOutput.getModel().getTemporaryName()
						: transporterVehicleOutput.getModel().getName())
				.engineTypeId(transporterVehicleOutput.getEngineType().getId())
				.engineTypeName(transporterVehicleOutput.getEngineType().getName()).photoUrl(vehiculeImageUrl)

				.build();

	}

}
