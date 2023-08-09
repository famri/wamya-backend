package com.excentria_it.wamya.adapter.persistence.adapter;

import com.excentria_it.wamya.adapter.persistence.entity.*;
import com.excentria_it.wamya.adapter.persistence.repository.*;
import com.excentria_it.wamya.application.port.out.AddVehiculePort;
import com.excentria_it.wamya.application.port.out.LoadVehiculePort;
import com.excentria_it.wamya.application.port.out.UpdateVehiculePort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.common.exception.VehicleNotFoundException;
import com.excentria_it.wamya.domain.AddVehiculeDto;
import com.excentria_it.wamya.domain.DocumentType;
import com.excentria_it.wamya.domain.EntitlementType;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@PersistenceAdapter
public class VehiclePersistenceAdapter implements AddVehiculePort, LoadVehiculePort, UpdateVehiculePort {

    private final TransporterRepository transporterRepository;

    private final EngineTypeRepository engineTypeRepository;

    private final ModelRepository modelRepository;

    private final VehicleRepository vehicleRepository;

    private final DocumentRepository documentRepository;

    @Override
    public AddVehiculeDto addVehicle(String transporterSubject, Long constructorId, Long modelId, Long engineTypeId,
                                     LocalDate circulationDate, String registration, String locale) {

        Optional<TransporterJpaEntity> transporterJpaEntityOptional = transporterRepository.findTransporterWithVehiclesBySubject(transporterSubject);

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

        transporterJpaEntity.addVehicle(VehicleJpaEntity.builder().type(engineTypeJpaEntity).model(modelJpaEntity)
                .circulationDate(circulationDate).registration(registration).build());

        transporterRepository.save(transporterJpaEntity);

        return AddVehiculeDto.builder().constructorName(modelJpaEntity.getConstructor().getName())
                .modelName(modelJpaEntity.getName()).engineType(engineTypeJpaEntity.getName(locale))
                .circulationDate(circulationDate).registration(registration).build();
    }

    @Override
    public AddVehiculeDto addVehicle(String transporterSubject, String constructorName, String modelName,
                                     Long engineTypeId, LocalDate circulationDate, String registration, String locale) {

        Optional<TransporterJpaEntity> transporterJpaEntityOptional = transporterRepository.findTransporterWithVehiclesBySubject(transporterSubject);

        if (transporterJpaEntityOptional.isEmpty()) {
            return null;
        }

        Optional<EngineTypeJpaEntity> engineTypeJpaEntityOptional = engineTypeRepository.findById(engineTypeId);

        if (engineTypeJpaEntityOptional.isEmpty()) {
            return null;
        }

        EngineTypeJpaEntity engineTypeJpaEntity = engineTypeJpaEntityOptional.get();

        TransporterJpaEntity transporterJpaEntity = transporterJpaEntityOptional.get();

        transporterJpaEntity.addVehicle(VehicleJpaEntity.builder().type(engineTypeJpaEntity)
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
        Optional<VehicleJpaEntity> vehiculeOptional = vehicleRepository.findById(vehiculeId);
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
        Optional<VehicleJpaEntity> vehiculeOptional = vehicleRepository.findById(vehiculeId);
        if (vehiculeOptional.isPresent()) {
            return vehiculeOptional.get().getImage() == null;
        } else {
            return false;
        }

    }

    @Override
    public void updateVehiculeImage(String transporterSubject, Long vehicleId, String location, String hash) {

        Optional<TransporterJpaEntity> transporterOptional = transporterRepository.findTransporterBySubject(transporterSubject);

        if (transporterOptional.isEmpty()) {
            throw new UserAccountNotFoundException(String.format("No account was found by username %s.", transporterSubject));
        }

        Optional<VehicleJpaEntity> vehicleOptional = vehicleRepository.findById(vehicleId);
        if (vehicleOptional.isPresent()) {
            VehicleJpaEntity vehicle = vehicleOptional.get();

            DocumentJpaEntity currentVehicleImage = vehicle.getImage();
            if (currentVehicleImage != null && !currentVehicleImage.getIsDefault()) {
                documentRepository.delete(currentVehicleImage);
            }
            EntitlementJpaEntity ownerEntitlement = new EntitlementJpaEntity(EntitlementType.OWNER, true, true);
            EntitlementJpaEntity othersEntitlement = new EntitlementJpaEntity(EntitlementType.OTHERS, true, false);
            EntitlementJpaEntity supportEntitlement = new EntitlementJpaEntity(EntitlementType.SUPPORT, true, true);

            Set<EntitlementJpaEntity> entitlements = Set.of(ownerEntitlement, othersEntitlement, supportEntitlement);

            DocumentJpaEntity newVehicleImage = new DocumentJpaEntity(transporterOptional.get(), location,
                    DocumentType.IMAGE_JPEG, Instant.now(), entitlements, hash, false);
            newVehicleImage = documentRepository.save(newVehicleImage);

            vehicle.setImage(newVehicleImage);
            vehicleRepository.save(vehicle);

        } else {
            throw new VehicleNotFoundException(String.format("No vehicle was found by ID %d.", vehicleId));
        }

    }

}
