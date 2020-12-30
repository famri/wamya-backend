package com.excentria_it.wamya.adapter.persistence.adapter;

import static com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData.*;
import static com.excentria_it.wamya.test.data.common.VehiculeJpaEntityTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.VehiculeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.VehiculeMapper;
import com.excentria_it.wamya.adapter.persistence.repository.TransporterRepository;
import com.excentria_it.wamya.domain.JourneyProposalDto.VehiculeDto;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ExtendWith(MockitoExtension.class)
public class TransporterPersistenceAdapterTests {
	@Mock
	private TransporterRepository transporterRepository;
	@Mock
	private VehiculeMapper vehiculeMapper;
	@InjectMocks
	private TransporterPersistenceAdapter transporterPersistenceAdapter;

	@Test
	void testLoadTransporterVehiculesByTransporterEmail() {
		// given

		TransporterJpaEntity transporterJpaEntity = defaultExistentTransporterJpaEntity();
		Set<VehiculeJpaEntity> vehiculeEntities = defaultVehiculeJpaEntitySet();
		transporterJpaEntity.setVehicules(vehiculeEntities);

		given(transporterRepository.findTransporterWithVehiculesByEmail(any(String.class)))
				.willReturn(transporterJpaEntity);

		List<VehiculeJpaEntity> vehiculeJpaEntities = new ArrayList<>(3);
		List<VehiculeDto> vehiculeDtos = new ArrayList<>(3);
		Iterator<VehiculeJpaEntity> it = vehiculeEntities.iterator();
		while (it.hasNext()) {
			VehiculeJpaEntity v = it.next();
			vehiculeJpaEntities.add(v);
			VehiculeDto vehiculeDto = new VehiculeDto(v.getId(), v.getModel().getConstructor().getName(),
					v.getModel().getName(), v.getPhotoUrl());
			vehiculeDtos.add(vehiculeDto);
			given(vehiculeMapper.mapToDomainEntity(v)).willReturn(vehiculeDto);
		}

		// when
		Set<VehiculeDto> vehicules = transporterPersistenceAdapter
				.loadTransporterVehicules(TestConstants.DEFAULT_EMAIL);
		// then

		assertTrue(vehiculeDtos.containsAll(vehicules) && vehiculeDtos.size() == vehicules.size());

	}

	@Test
	void testLoadTransporterEmptyVehiculesByTransporterEmail() {
		// given

		TransporterJpaEntity transporterJpaEntity = defaultExistentTransporterJpaEntity();

		transporterJpaEntity.setVehicules(Collections.<VehiculeJpaEntity>emptySet());

		given(transporterRepository.findTransporterWithVehiculesByEmail(any(String.class)))
				.willReturn(transporterJpaEntity);

		// when
		Set<VehiculeDto> vehicules = transporterPersistenceAdapter
				.loadTransporterVehicules(TestConstants.DEFAULT_EMAIL);
		// then

		assertTrue(vehicules.isEmpty());

	}

	@Test
	void testLoadTransporterVehiculesByTransporterMobileNumber() {
		// given

		TransporterJpaEntity transporterJpaEntity = defaultExistentTransporterJpaEntity();
		Set<VehiculeJpaEntity> vehiculeEntities = defaultVehiculeJpaEntitySet();
		transporterJpaEntity.setVehicules(vehiculeEntities);

		given(transporterRepository.findTransporterWithVehiculesByMobilePhoneNumber(any(String.class),
				any(String.class))).willReturn(transporterJpaEntity);

		List<VehiculeJpaEntity> vehiculeJpaEntities = new ArrayList<>(3);
		List<VehiculeDto> vehiculeDtos = new ArrayList<>(3);
		Iterator<VehiculeJpaEntity> it = vehiculeEntities.iterator();
		while (it.hasNext()) {
			VehiculeJpaEntity v = it.next();
			vehiculeJpaEntities.add(v);
			VehiculeDto vehiculeDto = new VehiculeDto(v.getId(), v.getModel().getConstructor().getName(),
					v.getModel().getName(), v.getPhotoUrl());
			vehiculeDtos.add(vehiculeDto);
			given(vehiculeMapper.mapToDomainEntity(v)).willReturn(vehiculeDto);
		}

		// when
		String[] mobileNumber = TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME.split("_");
		Set<VehiculeDto> vehicules = transporterPersistenceAdapter.loadTransporterVehicules(mobileNumber[0],
				mobileNumber[1]);
		// then

		assertTrue(vehiculeDtos.containsAll(vehicules) && vehiculeDtos.size() == vehicules.size());

	}

	@Test
	void testLoadTransporterEmptyVehiculesByTransporterMobileNumber() {
		// given

		TransporterJpaEntity transporterJpaEntity = defaultExistentTransporterJpaEntity();

		transporterJpaEntity.setVehicules(Collections.<VehiculeJpaEntity>emptySet());

		given(transporterRepository.findTransporterWithVehiculesByMobilePhoneNumber(any(String.class),
				any(String.class))).willReturn(transporterJpaEntity);

		// when
		String[] mobileNumber = TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME.split("_");
		Set<VehiculeDto> vehicules = transporterPersistenceAdapter.loadTransporterVehicules(mobileNumber[0],
				mobileNumber[1]);
		// then

		assertTrue(vehicules.isEmpty());

	}
}
