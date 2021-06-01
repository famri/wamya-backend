package com.excentria_it.wamya.adapter.persistence.adapter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import com.excentria_it.wamya.adapter.persistence.entity.DocumentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.EntitlementJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.ModelJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TemporaryModelJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.VehiculeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.repository.DocumentRepository;
import com.excentria_it.wamya.adapter.persistence.repository.EngineTypeRepository;
import com.excentria_it.wamya.adapter.persistence.repository.ModelRepository;
import com.excentria_it.wamya.adapter.persistence.repository.TransporterRepository;
import com.excentria_it.wamya.adapter.persistence.repository.VehiculeRepository;
import com.excentria_it.wamya.application.port.out.AddVehiculePort;
import com.excentria_it.wamya.application.port.out.LoadVehiculePort;
import com.excentria_it.wamya.application.port.out.UpdateVehiculePort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.common.exception.VehiculeNotFoundException;
import com.excentria_it.wamya.domain.AddVehiculeDto;
import com.excentria_it.wamya.domain.DocumentType;
import com.excentria_it.wamya.domain.EntitlementType;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class VehiculePersistenceAdapter implements AddVehiculePort, LoadVehiculePort, UpdateVehiculePort {

	private final TransporterRepository transporterRepository;

	private final EngineTypeRepository engineTypeRepository;

	private final ModelRepository modelRepository;

	private final VehiculeRepository vehiculeRepository;

	private final DocumentRepository documentRepository;

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
				.circulationDate(circulationDate).registration(registration).build());

		transporterRepository.save(transporterJpaEntity);

		return AddVehiculeDto.builder().constructorName(modelJpaEntity.getConstructor().getName())
				.modelName(modelJpaEntity.getName()).engineType(engineTypeJpaEntity.getName(locale))
				.circulationDate(circulationDate).registration(registration).build();
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
				.circulationDate(circulationDate).registration(registration).build());

		transporterRepository.save(transporterJpaEntity);

		return AddVehiculeDto.builder().constructorName(constructorName).modelName(modelName)
				.engineType(engineTypeJpaEntity.getName(locale)).circulationDate(circulationDate)
				.registration(registration).build();
	}

	@Override
	public String loadImageLocation(Long vehiculeId) {
		Optional<VehiculeJpaEntity> vehiculeOptional = vehiculeRepository.findById(vehiculeId);
		if (vehiculeOptional.isPresent()) {
			DocumentJpaEntity image = vehiculeOptional.get().getImage();
			if (image != null) {
				return image.getLocation();
			} else {
				return null;
			}
		}
		return null;
	}

	@Override
	public boolean hasDefaultVehiculeImage(Long vehiculeId) {
		Optional<VehiculeJpaEntity> vehiculeOptional = vehiculeRepository.findById(vehiculeId);
		if (vehiculeOptional.isPresent()) {
			return vehiculeOptional.get().getImage() == null;
		} else {
			return false;
		}

	}

	@Override
	public void updateVehiculeImage(String username, Long vehiculeId, String location, String hash) {

		Optional<TransporterJpaEntity> transporterOptional;
		if (username.contains("@")) {
			transporterOptional = transporterRepository.findByEmail(username);
		} else {
			String[] mobile = username.split("_");
			transporterOptional = transporterRepository.findByIcc_ValueAndMobileNumber(mobile[0], mobile[1]);
		}
		if (transporterOptional.isEmpty()) {
			throw new UserAccountNotFoundException(String.format("No account was found by username %s.", username));
		}

		Optional<VehiculeJpaEntity> vehiculeOptional = vehiculeRepository.findById(vehiculeId);
		if (vehiculeOptional.isPresent()) {
			VehiculeJpaEntity vehicule = vehiculeOptional.get();

			DocumentJpaEntity currentVehiculeImage = vehicule.getImage();
			if (currentVehiculeImage != null && !currentVehiculeImage.getIsDefault()) {
				documentRepository.delete(currentVehiculeImage);
			}
			EntitlementJpaEntity ownerEntitlement = new EntitlementJpaEntity(EntitlementType.OWNER, true, true);
			EntitlementJpaEntity othersEntitlement = new EntitlementJpaEntity(EntitlementType.OTHERS, true, false);
			EntitlementJpaEntity supportEntitlement = new EntitlementJpaEntity(EntitlementType.SUPPORT, true, true);

			Set<EntitlementJpaEntity> entitlements = Set.of(ownerEntitlement, othersEntitlement, supportEntitlement);

			DocumentJpaEntity newVehiculeImage = new DocumentJpaEntity(transporterOptional.get(), location,
					DocumentType.IMAGE_JPEG, Instant.now(), entitlements, hash, false);
			newVehiculeImage = documentRepository.save(newVehiculeImage);

			vehicule.setImage(newVehiculeImage);
			vehiculeRepository.save(vehicule);

		} else {
			throw new VehiculeNotFoundException(String.format("No account was found by username %d.", vehiculeId));
		}

	}

}
