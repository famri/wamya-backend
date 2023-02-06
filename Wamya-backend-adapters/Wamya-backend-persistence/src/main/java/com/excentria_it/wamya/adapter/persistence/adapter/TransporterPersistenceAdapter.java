package com.excentria_it.wamya.adapter.persistence.adapter;

import com.excentria_it.wamya.adapter.persistence.mapper.VehiculeMapper;
import com.excentria_it.wamya.adapter.persistence.repository.TransporterRepository;
import com.excentria_it.wamya.application.port.out.CheckUserVehiclePort;
import com.excentria_it.wamya.application.port.out.LoadTransporterVehiculesPort;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.common.utils.ParameterUtils;
import com.excentria_it.wamya.domain.LoadTransporterVehiclesCriteria;
import com.excentria_it.wamya.domain.TransporterVehicleOutput;
import com.excentria_it.wamya.domain.TransporterVehicles;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@PersistenceAdapter
public class TransporterPersistenceAdapter implements LoadTransporterVehiculesPort, CheckUserVehiclePort {

    private final TransporterRepository transporterRepository;
    private final VehiculeMapper vehiculeMapper;

    @Override
    public Boolean isUserVehicle(String subject, Long vehicleId) {

        if (subject == null || vehicleId == null) {
            return false;
        }

        return transporterRepository.existsBySubjectAndVehicleId(subject, vehicleId);

    }

    @Override
    public TransporterVehicles loadTransporterVehicules(LoadTransporterVehiclesCriteria criteria, String locale) {

        Sort sort = convertToSort(criteria.getSortingCriterion());

        List<TransporterVehicleOutput> vehicules = transporterRepository
                .findTransporterVehiclesByEmail(criteria.getTransporterUsername(), locale, sort);


        return new TransporterVehicles(
                vehicules.stream().map(v -> vehiculeMapper.mapToDomainEntity(v))
                        .collect(Collectors.toList()));


    }

    protected Sort convertToSort(SortCriterion sortingCriterion) {

        return Sort.by(Direction.valueOf(sortingCriterion.getDirection().toUpperCase()),
                ParameterUtils.kebabToCamelCase(sortingCriterion.getField()));
    }

}
