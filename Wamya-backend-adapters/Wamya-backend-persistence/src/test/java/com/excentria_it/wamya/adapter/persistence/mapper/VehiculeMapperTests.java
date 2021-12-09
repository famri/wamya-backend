package com.excentria_it.wamya.adapter.persistence.mapper;

import static com.excentria_it.wamya.test.data.common.VehiculeJpaEntityTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.application.utils.DocumentUrlResolver;
import com.excentria_it.wamya.domain.TransporterVehiculeDto;
import com.excentria_it.wamya.domain.TransporterVehiculeOutput;

public class VehiculeMapperTests {
	private DocumentUrlResolver documentUrlResolver = new DocumentUrlResolver();

	private VehiculeMapper vehiculeMapper = new VehiculeMapper(documentUrlResolver);

	@BeforeEach
	void initDocumentUrlResolver() {
		documentUrlResolver.setServerBaseUrl("https://domain-name:port/wamya-backend");
	}

	@Test
	void testMapToDomainEntity() {

		// given
		TransporterVehiculeOutput tvo = defaultTransporterVehiculeOutput();

		// when
		TransporterVehiculeDto tvDto = vehiculeMapper.mapToDomainEntity(tvo);

		// then
		assertEquals(tvo.getId(), tvDto.getId());
		assertEquals(tvo.getCirculationDate(), tvDto.getCirculationDate());
		assertEquals(tvo.getConstructor().getName(), tvDto.getConstructorName());
		assertEquals(tvo.getModel().getName(), tvDto.getModelName());
		assertEquals(tvo.getEngineType().getName(), tvDto.getEngineTypeName());
		assertEquals(getVehiculeImageUrl(tvo), tvDto.getPhotoUrl());
	}

	@Test
	void givenNoConstructorAndNoModelAndNoImage_whentMapToDomainEntity_thenReturnTransporterVehiculeDto() {

		// given
		TransporterVehiculeOutput tvo = transporterVehiculeOutputWithTemporaryConstructorAndModelAndNoImage();

		// when
		TransporterVehiculeDto tvDto = vehiculeMapper.mapToDomainEntity(tvo);

		// then
		assertEquals(tvo.getId(), tvDto.getId());
		assertEquals(tvo.getCirculationDate(), tvDto.getCirculationDate());
		assertEquals(tvo.getConstructor().getTemporaryName(), tvDto.getConstructorName());
		assertEquals(tvo.getModel().getTemporaryName(), tvDto.getModelName());
		assertEquals(tvo.getEngineType().getName(), tvDto.getEngineTypeName());
		assertEquals(getVehiculeImageUrl(tvo), tvDto.getPhotoUrl());
	}

	private String getVehiculeImageUrl(TransporterVehiculeOutput transporterVehiculeOutput) {
		return (transporterVehiculeOutput.getImage().getId() != null
				&& transporterVehiculeOutput.getImage().getHash() != null)
						? documentUrlResolver.resolveUrl(transporterVehiculeOutput.getImage().getId(),
								transporterVehiculeOutput.getImage().getHash())
						: documentUrlResolver.resolveUrl(transporterVehiculeOutput.getEngineType().getImageId(),
								transporterVehiculeOutput.getEngineType().getImageHash());
	}
}
