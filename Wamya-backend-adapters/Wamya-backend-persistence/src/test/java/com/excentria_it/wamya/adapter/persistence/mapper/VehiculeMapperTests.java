package com.excentria_it.wamya.adapter.persistence.mapper;

import static com.excentria_it.wamya.test.data.common.VehiculeJpaEntityTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.adapter.persistence.entity.TemporaryModelJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.VehiculeJpaEntity;
import com.excentria_it.wamya.application.utils.DocumentUrlResolver;
import com.excentria_it.wamya.domain.JourneyProposalDto.VehiculeDto;
import com.excentria_it.wamya.test.data.common.DocumentJpaTestData;

public class VehiculeMapperTests {
	private DocumentUrlResolver documentUrlResolver = new DocumentUrlResolver();

	private VehiculeMapper vehiculeMapper = new VehiculeMapper(documentUrlResolver);

	@BeforeEach
	void initDocumentUrlResolver() {
		documentUrlResolver.setServerBaseUrl("https://domain-name:port/wamya-backend");
	}

	@Test
	void testMapVehiculeJpaEntityWithExistentConstructorAndModelToDomainEntity() {
		VehiculeJpaEntity vehiculeJpaEntity = defaultVehiculeJpaEntity();
		VehiculeDto vehiculeDto = vehiculeMapper.mapToDomainEntity(vehiculeJpaEntity);

		assertEquals(vehiculeJpaEntity.getId(), vehiculeDto.getId());
		assertEquals(vehiculeJpaEntity.getModel().getName(), vehiculeDto.getModel());
		assertEquals(vehiculeJpaEntity.getModel().getConstructor().getName(), vehiculeDto.getConstructor());
		assertEquals(getVehiculeImageUrl(vehiculeJpaEntity), vehiculeDto.getPhotoUrl());

	}

	@Test
	void testMapVehiculeJpaEntityWithNonDefaultVehiculeImage() {
		VehiculeJpaEntity vehiculeJpaEntity = defaultVehiculeJpaEntityBuilder()
				.image(DocumentJpaTestData.nonDefaultVehiculeImageDocumentJpaEntity()).build();
		VehiculeDto vehiculeDto = vehiculeMapper.mapToDomainEntity(vehiculeJpaEntity);

		assertEquals(vehiculeJpaEntity.getId(), vehiculeDto.getId());
		assertEquals(vehiculeJpaEntity.getModel().getName(), vehiculeDto.getModel());
		assertEquals(vehiculeJpaEntity.getModel().getConstructor().getName(), vehiculeDto.getConstructor());
		assertEquals(getVehiculeImageUrl(vehiculeJpaEntity), vehiculeDto.getPhotoUrl());

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
		assertEquals(getVehiculeImageUrl(vehiculeJpaEntity), vehiculeDto.getPhotoUrl());

	}

	private String getVehiculeImageUrl(VehiculeJpaEntity vehicule) {
		return (vehicule.getImage() != null)
				? documentUrlResolver.resolveUrl(vehicule.getImage().getId(), vehicule.getImage().getHash())
				: documentUrlResolver.resolveUrl(vehicule.getType().getImage().getId(),
						vehicule.getType().getImage().getHash());
	}
}
