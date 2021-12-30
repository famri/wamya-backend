package com.excentria_it.wamya.adapter.persistence.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.excentria_it.wamya.adapter.persistence.mapper.VehiculeMapper;
import com.excentria_it.wamya.adapter.persistence.repository.TransporterRepository;
import com.excentria_it.wamya.application.port.out.CheckUserVehiculePort;
import com.excentria_it.wamya.application.port.out.LoadTransporterVehiculesPort;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.common.utils.ParameterUtils;
import com.excentria_it.wamya.domain.LoadTransporterVehiculesCriteria;
import com.excentria_it.wamya.domain.TransporterVehiculeOutput;
import com.excentria_it.wamya.domain.TransporterVehicules;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class TransporterPersistenceAdapter implements LoadTransporterVehiculesPort, CheckUserVehiculePort {

	private final TransporterRepository transporterRepository;
	private final VehiculeMapper vehiculeMapper;

	@Override
	public Boolean isUserVehicule(String username, Long vehiculeId) {

		if (username == null || vehiculeId == null) {
			return false;
		}

		if (username.contains("@")) {
			return transporterRepository.existsByEmailAndVehiculeId(username, vehiculeId);
		} else {
			return false;
		}

	}

	@Override
	public TransporterVehicules loadTransporterVehicules(LoadTransporterVehiculesCriteria criteria, String locale) {

		Sort sort = convertToSort(criteria.getSortingCriterion());

		List<TransporterVehiculeOutput> vehicules = transporterRepository
				.findTransporterVehiculesByEmail(criteria.getTransporterUsername(), locale, sort);

		

			return new TransporterVehicules(
					vehicules.stream().map(v -> vehiculeMapper.mapToDomainEntity(v))
							.collect(Collectors.toList()));
		

	}

	protected Sort convertToSort(SortCriterion sortingCriterion) {

		return Sort.by(Direction.valueOf(sortingCriterion.getDirection().toUpperCase()),
				ParameterUtils.kebabToCamelCase(sortingCriterion.getField()));
	}

}
