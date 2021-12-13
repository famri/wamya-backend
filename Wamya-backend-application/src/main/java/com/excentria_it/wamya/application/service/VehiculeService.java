package com.excentria_it.wamya.application.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;

import com.excentria_it.wamya.application.port.in.AddVehiculeUseCase;
import com.excentria_it.wamya.application.port.in.LoadVehiculesUseCase;
import com.excentria_it.wamya.application.port.out.AddVehiculePort;
import com.excentria_it.wamya.application.port.out.LoadConstructorPort;
import com.excentria_it.wamya.application.port.out.LoadEngineTypePort;
import com.excentria_it.wamya.application.port.out.LoadTransporterVehiculesPort;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.exception.ConstructorModelNotFoundException;
import com.excentria_it.wamya.common.exception.ConstructorNotFoundException;
import com.excentria_it.wamya.common.exception.EngineTypeNotFoundException;
import com.excentria_it.wamya.domain.AddVehiculeDto;
import com.excentria_it.wamya.domain.ConstructorDto;
import com.excentria_it.wamya.domain.EngineTypeDto;
import com.excentria_it.wamya.domain.LoadTransporterVehiculesCriteria;
import com.excentria_it.wamya.domain.TransporterVehicules;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class VehiculeService implements AddVehiculeUseCase, LoadVehiculesUseCase {

	private final AddVehiculePort addVehiculePort;

	private final LoadConstructorPort loadConstructorPort;

	private final LoadEngineTypePort loadEngineTypePort;

	private final LoadTransporterVehiculesPort loadTransporterVehiculesPort;

	@Override
	public TransporterVehicules loadTransporterVehicules(LoadVehiculesCommand command, String locale) {
		LoadTransporterVehiculesCriteria criteria = LoadTransporterVehiculesCriteria.builder()
				.transporterUsername(command.getTransporterUsername())
				.sortingCriterion(command.getSortingCriterion()).build();

		return loadTransporterVehiculesPort.loadTransporterVehicules(criteria, locale);

	}

	@Override
	public AddVehiculeDto addVehicule(AddVehiculeCommand command, String transporterUsername, String locale) {

		// check if engineTypeId exists
		checkEngineType(command.getEngineTypeId(), locale);

		// check if constructor id and model id exist
		boolean constructorAndModelExist = checkConstructorAndModel(command.getConstructorId(), command.getModelId(),
				command.getConstructorName(), command.getModelName());

		AddVehiculeDto addVehiculeDto = null;
		if (constructorAndModelExist) {
			addVehiculeDto = addVehiculePort.addVehicule(transporterUsername, command.getConstructorId(),
					command.getModelId(), command.getEngineTypeId(), command.getCirculationDate(),
					command.getRegistration(), locale);
		} else {
			addVehiculeDto = addVehiculePort.addVehicule(transporterUsername, command.getConstructorName(),
					command.getModelName(), command.getEngineTypeId(), command.getCirculationDate(),
					command.getRegistration(), locale);
		}

		return addVehiculeDto;
	}

	private void checkEngineType(Long engineTypeId, String locale) {
		Optional<EngineTypeDto> engineTypeDtoOptional = loadEngineTypePort.loadEngineTypeById(engineTypeId, locale);
		if (engineTypeDtoOptional.isEmpty()) {
			throw new EngineTypeNotFoundException(String.format("Engine type not found: %d", engineTypeId));
		}
	}

	private boolean checkConstructorAndModel(Long constructorId, Long modelId, String constructorName,
			String modelName) {
		if (constructorId != null && modelId != null) {
			Optional<ConstructorDto> constructorDtoOptional = loadConstructorPort.loadConstructorById(constructorId);

			if (constructorDtoOptional.isEmpty()) {
				throw new ConstructorNotFoundException(String.format("Constructor not found: %d", constructorId));

			}

			ConstructorDto constructorDto = constructorDtoOptional.get();
			boolean modelExists = constructorDto.getModels().stream().anyMatch(m -> m.getId().equals(modelId));
			if (!modelExists) {
				throw new ConstructorModelNotFoundException(String.format("Constructor model not found: %d", modelId));
			}

			return true;

		} else {
			if (StringUtils.isEmpty(constructorName) || StringUtils.isEmpty(modelName)) {
				throw new ConstructorModelNotFoundException(
						"Constructor and model are mandatory if no constructorId nor modelId were specified.");
			}

			return false;
		}

	}

}
