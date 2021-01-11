package com.excentria_it.wamya.adapter.persistence.mapper;

import static com.excentria_it.wamya.test.data.common.VehiculeJpaEntityTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.adapter.persistence.entity.TemporaryModelJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.VehiculeJpaEntity;
import com.excentria_it.wamya.domain.JourneyProposalDto.VehiculeDto;

public class VehiculeMapperTests {

	private VehiculeMapper vehiculeMapper = new VehiculeMapper();

	@Test
	void testMapVehiculeJpaEntityWithExistentConstructorAndModelToDomainEntity() {
		VehiculeJpaEntity vehiculeJpaEntity = defaultVehiculeJpaEntity();
		VehiculeDto vehiculeDto = vehiculeMapper.mapToDomainEntity(vehiculeJpaEntity);

		assertEquals(vehiculeJpaEntity.getId(), vehiculeDto.getId());
		assertEquals(vehiculeJpaEntity.getModel().getName(), vehiculeDto.getModel());
		assertEquals(vehiculeJpaEntity.getModel().getConstructor().getName(), vehiculeDto.getConstructor());
		assertEquals(vehiculeJpaEntity.getPhotoUrl(), vehiculeDto.getPhotoUrl());

	}

	@Test
	void testMapVehiculeJpaEntityWithInexistentConstructorAndModelToDomainEntity() {
		VehiculeJpaEntity vehiculeJpaEntity = defaultVehiculeJpaEntity();
		vehiculeJpaEntity.setModel(null);
		vehiculeJpaEntity.setTemporaryModel(
				TemporaryModelJpaEntity.builder().id(1L).constructorName("Constructor1").modelName("Model1").build());
		VehiculeDto vehiculeDto = vehiculeMapper.mapToDomainEntity(vehiculeJpaEntity);

		assertEquals(vehiculeJpaEntity.getId(), vehiculeDto.getId());
		assertEquals(vehiculeJpaEntity.getTemporaryModel().getModelName(), vehiculeDto.getModel());
		assertEquals(vehiculeJpaEntity.getTemporaryModel().getConstructorName(), vehiculeDto.getConstructor());
		assertEquals(vehiculeJpaEntity.getPhotoUrl(), vehiculeDto.getPhotoUrl());

	}
}
