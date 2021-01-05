package com.excentria_it.wamya.adapter.persistence.adapter;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.VehiculeMapper;
import com.excentria_it.wamya.adapter.persistence.repository.TransporterRepository;
import com.excentria_it.wamya.application.port.out.LoadTransporterVehiculesPort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.domain.JourneyProposalDto.VehiculeDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class TransporterPersistenceAdapter implements LoadTransporterVehiculesPort {

	private final TransporterRepository transporterRepository;

	private final VehiculeMapper vehiculeMapper;

	@Override
	public Set<VehiculeDto> loadTransporterVehicules(String transporterEmail) {

		Optional<TransporterJpaEntity> transporter = transporterRepository
				.findTransporterWithVehiculesByEmail(transporterEmail);

		if (transporter.isEmpty())
			return Collections.<VehiculeDto>emptySet();

		return transporter.get().getVehicules().stream().map(v -> vehiculeMapper.mapToDomainEntity(v))
				.collect(Collectors.toSet());
	}

	@Override
	public Set<VehiculeDto> loadTransporterVehicules(String transporterIcc, String transporterMobileNumber) {

		Optional<TransporterJpaEntity> transporter = transporterRepository
				.findTransporterWithVehiculesByMobilePhoneNumber(transporterIcc, transporterMobileNumber);

		if (transporter.isEmpty())
			return Collections.<VehiculeDto>emptySet();

		return transporter.get().getVehicules().stream().map(v -> vehiculeMapper.mapToDomainEntity(v))
				.collect(Collectors.toSet());
	}

}
