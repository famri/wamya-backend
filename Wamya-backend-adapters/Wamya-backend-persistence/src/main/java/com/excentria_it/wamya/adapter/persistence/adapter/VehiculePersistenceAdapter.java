package com.excentria_it.wamya.adapter.persistence.adapter;

import java.time.LocalDate;
import java.util.Optional;

import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.ModelJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TemporaryModelJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.VehiculeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.repository.EngineTypeRepository;
import com.excentria_it.wamya.adapter.persistence.repository.ModelRepository;
import com.excentria_it.wamya.adapter.persistence.repository.TransporterRepository;
import com.excentria_it.wamya.application.port.out.AddVehiculePort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.domain.AddVehiculeDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class VehiculePersistenceAdapter implements AddVehiculePort {

	private final TransporterRepository transporterRepository;

	private final EngineTypeRepository engineTypeRepository;

	private final ModelRepository modelRepository;

	@Override
	public AddVehiculeDto addVehicule(String transporterUsername, Long constructorId, Long modelId, Long engineTypeId,
			LocalDate circulationDate, String registration, String locale) {

		Optional<TransporterJpaEntity> transporterJpaEntityOptional = null;
		if (transporterUsername.contains("@")) {
			transporterJpaEntityOptional = transporterRepository
					.findTransporterWithVehiculesByEmail(transporterUsername);
			
		} else {

			String[] mobileNumber = transporterUsername.split("_");
			transporterJpaEntityOptional = transporterRepository
					.findTransporterWithVehiculesByMobilePhoneNumber(mobileNumber[0], mobileNumber[1]);
		}

		if (transporterJpaEntityOptional.isEmpty()) {
			return null;
		}

		Optional<EngineTypeJpaEntity> engineTypeJpaEntityOptional = engineTypeRepository.findById(engineTypeId);

		if (engineTypeJpaEntityOptional.isEmpty()) {
			return null;
		}

		EngineTypeJpaEntity engineTypeJpaEntity = engineTypeJpaEntityOptional.get();

		Optional<ModelJpaEntity> modelJpaEntityOptional = modelRepository.findById(modelId);

		if (modelJpaEntityOptional.isEmpty()) {
			return null;
		}

		ModelJpaEntity modelJpaEntity = modelJpaEntityOptional.get();

		TransporterJpaEntity transporterJpaEntity = transporterJpaEntityOptional.get();

		transporterJpaEntity.addVehicule(VehiculeJpaEntity.builder().type(engineTypeJpaEntity).model(modelJpaEntity)
				.photoUrl(engineTypeJpaEntity.getDefaultPhotoUri()).circulationDate(circulationDate)
				.registration(registration).build());

		transporterRepository.save(transporterJpaEntity);

		return AddVehiculeDto.builder().constructorName(modelJpaEntity.getConstructor().getName())
				.modelName(modelJpaEntity.getName()).engineType(engineTypeJpaEntity.getName(locale))
				.circulationDate(circulationDate).registration(registration)
				.photoUrl(engineTypeJpaEntity.getDefaultPhotoUri()).build();
	}

	@Override
	public AddVehiculeDto addVehicule(String transporterUsername, String constructorName, String modelName,
			Long engineTypeId, LocalDate circulationDate, String registration, String locale) {

		Optional<TransporterJpaEntity> transporterJpaEntityOptional = null;
		if (transporterUsername.contains("@")) {
			transporterJpaEntityOptional = transporterRepository
					.findTransporterWithVehiculesByEmail(transporterUsername);

		} else {

			String[] mobileNumber = transporterUsername.split("_");
			transporterJpaEntityOptional = transporterRepository
					.findTransporterWithVehiculesByMobilePhoneNumber(mobileNumber[0], mobileNumber[1]);
		}

		if (transporterJpaEntityOptional.isEmpty()) {
			return null;
		}

		Optional<EngineTypeJpaEntity> engineTypeJpaEntityOptional = engineTypeRepository.findById(engineTypeId);

		if (engineTypeJpaEntityOptional.isEmpty()) {
			return null;
		}

		EngineTypeJpaEntity engineTypeJpaEntity = engineTypeJpaEntityOptional.get();

		TransporterJpaEntity transporterJpaEntity = transporterJpaEntityOptional.get();

		transporterJpaEntity.addVehicule(VehiculeJpaEntity.builder().type(engineTypeJpaEntity)
				.temporaryModel(
						TemporaryModelJpaEntity.builder().constructorName(constructorName).modelName(modelName).build())
				.photoUrl(engineTypeJpaEntity.getDefaultPhotoUri()).circulationDate(circulationDate)
				.registration(registration).build());

		transporterRepository.save(transporterJpaEntity);

		return AddVehiculeDto.builder().constructorName(constructorName).modelName(modelName)
				.engineType(engineTypeJpaEntity.getName(locale)).circulationDate(circulationDate)
				.registration(registration).photoUrl(engineTypeJpaEntity.getDefaultPhotoUri()).build();
	}

}
