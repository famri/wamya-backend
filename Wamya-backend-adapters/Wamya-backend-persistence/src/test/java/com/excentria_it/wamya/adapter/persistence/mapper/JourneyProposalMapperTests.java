package com.excentria_it.wamya.adapter.persistence.mapper;

import static com.excentria_it.wamya.test.data.common.JourneyProposalJpaEntityTestData.*;
import static com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData.*;
import static com.excentria_it.wamya.test.data.common.VehiculeJpaEntityTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.VehiculeJpaEntity;
import com.excentria_it.wamya.application.utils.DocumentUrlResolver;
import com.excentria_it.wamya.domain.JourneyProposalDto;
import com.excentria_it.wamya.domain.TransporterProposalOutput;
import com.excentria_it.wamya.test.data.common.DocumentJpaTestData;
import com.excentria_it.wamya.test.data.common.JourneyRequestJpaTestData;
import com.excentria_it.wamya.test.data.common.VehiculeJpaEntityTestData;

public class JourneyProposalMapperTests {
	private DocumentUrlResolver documentUrlResolver = new DocumentUrlResolver();
	private JourneyProposalMapper journeyProposalMapper = new JourneyProposalMapper(documentUrlResolver);
	private static final Double JOURNEY_PRICE = 250.0;

	@BeforeEach
	void initDocumentUrlResolver() {
		documentUrlResolver.setServerBaseUrl("https://domain-name:port/wamya-backend");
	}

	@Test
	void testMapToJpaEntity() {

		TransporterJpaEntity transporterJpaEntity = defaultExistentTransporterJpaEntity();
		VehiculeJpaEntity vehiculeJpaEntity = defaultVehiculeJpaEntity();

		JourneyProposalJpaEntity journeyProposalJpaEntity = journeyProposalMapper.mapToJpaEntity(JOURNEY_PRICE,
				transporterJpaEntity, vehiculeJpaEntity);

		assertEquals(JOURNEY_PRICE, journeyProposalJpaEntity.getPrice());

		assertEquals(transporterJpaEntity, journeyProposalJpaEntity.getTransporter());

		assertEquals(vehiculeJpaEntity, journeyProposalJpaEntity.getVehicule());
	}

	@Test
	void testMapToJourneyProposalDto() {

		JourneyProposalJpaEntity journeyProposalJpaEntity = defaultJourneyProposalJpaEntity();

		JourneyProposalDto journeyProposalDto = journeyProposalMapper.mapToJourneyProposalDto(journeyProposalJpaEntity,
				"en_US");

		assertEquals(journeyProposalJpaEntity.getId(), journeyProposalDto.getId());
		assertEquals(journeyProposalJpaEntity.getPrice(), journeyProposalDto.getPrice());

		assertEquals(journeyProposalJpaEntity.getTransporter().getOauthId(),
				journeyProposalDto.getTransporter().getId());
		assertEquals(journeyProposalJpaEntity.getTransporter().getFirstname(),
				journeyProposalDto.getTransporter().getFirstname());
		assertEquals(journeyProposalJpaEntity.getTransporter().getGlobalRating(),
				journeyProposalDto.getTransporter().getGlobalRating());
		assertEquals(
				documentUrlResolver.resolveUrl(journeyProposalJpaEntity.getTransporter().getProfileImage().getId(),
						journeyProposalJpaEntity.getTransporter().getProfileImage().getHash()),
				journeyProposalDto.getTransporter().getPhotoUrl());

		assertEquals(journeyProposalJpaEntity.getVehicule().getId(), journeyProposalDto.getVehicule().getId());
		assertEquals(journeyProposalJpaEntity.getVehicule().getModel().getConstructor().getName(),
				journeyProposalDto.getVehicule().getConstructor());
		assertEquals(journeyProposalJpaEntity.getVehicule().getModel().getName(),
				journeyProposalDto.getVehicule().getModel());
		assertEquals(getVehiculeImageUrl(journeyProposalJpaEntity.getVehicule()),
				journeyProposalDto.getVehicule().getPhotoUrl());
	}

	@Test
	void testMapToDomainEntityWithNonDefaultVehiculeImage() {

		JourneyProposalJpaEntity journeyProposalJpaEntity = defaultJourneyProposalJpaEntityBuilder()
				.vehicule(VehiculeJpaEntityTestData.defaultVehiculeJpaEntityBuilder()
						.image(DocumentJpaTestData.nonDefaultVehiculeImageDocumentJpaEntity()).build())
				.build();

		JourneyProposalDto journeyProposalDto = journeyProposalMapper.mapToJourneyProposalDto(journeyProposalJpaEntity,
				"en_US");

		assertEquals(journeyProposalJpaEntity.getId(), journeyProposalDto.getId());
		assertEquals(journeyProposalJpaEntity.getPrice(), journeyProposalDto.getPrice());

		assertEquals(journeyProposalJpaEntity.getTransporter().getOauthId(),
				journeyProposalDto.getTransporter().getId());
		assertEquals(journeyProposalJpaEntity.getTransporter().getFirstname(),
				journeyProposalDto.getTransporter().getFirstname());
		assertEquals(journeyProposalJpaEntity.getTransporter().getGlobalRating(),
				journeyProposalDto.getTransporter().getGlobalRating());
		assertEquals(
				documentUrlResolver.resolveUrl(journeyProposalJpaEntity.getTransporter().getProfileImage().getId(),
						journeyProposalJpaEntity.getTransporter().getProfileImage().getHash()),
				journeyProposalDto.getTransporter().getPhotoUrl());

		assertEquals(journeyProposalJpaEntity.getVehicule().getId(), journeyProposalDto.getVehicule().getId());
		assertEquals(journeyProposalJpaEntity.getVehicule().getModel().getConstructor().getName(),
				journeyProposalDto.getVehicule().getConstructor());
		assertEquals(journeyProposalJpaEntity.getVehicule().getModel().getName(),
				journeyProposalDto.getVehicule().getModel());
		assertEquals(getVehiculeImageUrl(journeyProposalJpaEntity.getVehicule()),
				journeyProposalDto.getVehicule().getPhotoUrl());
	}

	@Test
	void testMapNullToDomainEntity() {

		JourneyProposalDto journeyProposalDto = journeyProposalMapper.mapToJourneyProposalDto(null, "en_US");

		assertNull(journeyProposalDto);
	}

	private String getVehiculeImageUrl(VehiculeJpaEntity vehicule) {
		return (vehicule.getImage() != null)
				? documentUrlResolver.resolveUrl(vehicule.getImage().getId(), vehicule.getImage().getHash())
				: documentUrlResolver.resolveUrl(vehicule.getType().getImage().getId(),
						vehicule.getType().getImage().getHash());
	}

	@Test
	void testMapNullToTransporterProposalOutput() {

		TransporterProposalOutput journeyProposalOutput = journeyProposalMapper.mapToTransporterProposalOutput(null,
				"en_US");

		assertNull(journeyProposalOutput);
	}

	@Test
	void testMapToTransporterProposalOutput() {
		JourneyProposalJpaEntity journeyProposalJpaEntity = defaultJourneyProposalJpaEntityBuilder()
				.vehicule(VehiculeJpaEntityTestData.defaultVehiculeJpaEntityBuilder()
						.image(DocumentJpaTestData.nonDefaultVehiculeImageDocumentJpaEntity()).build())
				.journeyRequest(JourneyRequestJpaTestData.defaultExistentJourneyRequestJpaEntity()).build();

		TransporterProposalOutput journeyProposalOutput = journeyProposalMapper
				.mapToTransporterProposalOutput(journeyProposalJpaEntity, "en_US");

		assertEquals(journeyProposalJpaEntity.getId(), journeyProposalOutput.getId());
		assertEquals(journeyProposalJpaEntity.getPrice(), journeyProposalOutput.getPrice());
		assertEquals(journeyProposalJpaEntity.getStatus().getCode(), journeyProposalOutput.getStatusCode());
		assertEquals(journeyProposalJpaEntity.getStatus().getValue("en_US"), journeyProposalOutput.getStatus());

		assertEquals(journeyProposalJpaEntity.getVehicule().getId(), journeyProposalOutput.getVehicule().getId());
		assertEquals(journeyProposalJpaEntity.getVehicule().getRegistration(),
				journeyProposalOutput.getVehicule().getRegistrationNumber());
		assertEquals(journeyProposalJpaEntity.getVehicule().getCirculationDate(),
				journeyProposalOutput.getVehicule().getCirculationDate());

		assertEquals(journeyProposalJpaEntity.getVehicule().getModel().getConstructor().getName(),
				journeyProposalOutput.getVehicule().getConstructorName());
		assertEquals(journeyProposalJpaEntity.getVehicule().getModel().getName(),
				journeyProposalOutput.getVehicule().getModelName());

		assertEquals(journeyProposalJpaEntity.getVehicule().getType().getId(),
				journeyProposalOutput.getVehicule().getEngineTypeId());

		assertEquals(journeyProposalJpaEntity.getVehicule().getType().getName("en_US"),
				journeyProposalOutput.getVehicule().getEngineTypeName());

		assertEquals(journeyProposalJpaEntity.getVehicule().getImage().getId(),
				journeyProposalOutput.getVehicule().getImageId());
		assertEquals(journeyProposalJpaEntity.getVehicule().getImage().getHash(),
				journeyProposalOutput.getVehicule().getImageHash());

		assertEquals(journeyProposalJpaEntity.getJourneyRequest().getId(), journeyProposalOutput.getJourney().getId());
		assertEquals(journeyProposalJpaEntity.getJourneyRequest().getDistance(),
				journeyProposalOutput.getJourney().getDistance());
		assertEquals(journeyProposalJpaEntity.getJourneyRequest().getHours(),
				journeyProposalOutput.getJourney().getHours());
		assertEquals(journeyProposalJpaEntity.getJourneyRequest().getMinutes(),
				journeyProposalOutput.getJourney().getMinutes());
		assertEquals(journeyProposalJpaEntity.getJourneyRequest().getDateTime(),
				journeyProposalOutput.getJourney().getDateTime());
		assertEquals(journeyProposalJpaEntity.getJourneyRequest().getWorkers(),
				journeyProposalOutput.getJourney().getWorkers());
		assertEquals(journeyProposalJpaEntity.getJourneyRequest().getDescription(),
				journeyProposalOutput.getJourney().getDescription());

		assertEquals(journeyProposalJpaEntity.getJourneyRequest().getDeparturePlace().getPlaceId().getId(),
				journeyProposalOutput.getJourney().getDeparturePlace().getId());
		assertEquals(journeyProposalJpaEntity.getJourneyRequest().getDeparturePlace().getPlaceId().getType().name(),
				journeyProposalOutput.getJourney().getDeparturePlace().getType());
		assertEquals(journeyProposalJpaEntity.getJourneyRequest().getDeparturePlace().getName("en_US"),
				journeyProposalOutput.getJourney().getDeparturePlace().getName());
		assertEquals(journeyProposalJpaEntity.getJourneyRequest().getDeparturePlace().getDepartment().getId(),
				journeyProposalOutput.getJourney().getDeparturePlace().getDepartmentId());
		assertEquals(journeyProposalJpaEntity.getJourneyRequest().getDeparturePlace().getLatitude(),
				journeyProposalOutput.getJourney().getDeparturePlace().getLatitude());
		assertEquals(journeyProposalJpaEntity.getJourneyRequest().getDeparturePlace().getLongitude(),
				journeyProposalOutput.getJourney().getDeparturePlace().getLongitude());

		assertEquals(journeyProposalJpaEntity.getJourneyRequest().getArrivalPlace().getPlaceId().getId(),
				journeyProposalOutput.getJourney().getArrivalPlace().getId());
		assertEquals(journeyProposalJpaEntity.getJourneyRequest().getArrivalPlace().getPlaceId().getType().name(),
				journeyProposalOutput.getJourney().getArrivalPlace().getType());
		assertEquals(journeyProposalJpaEntity.getJourneyRequest().getArrivalPlace().getName("en_US"),
				journeyProposalOutput.getJourney().getArrivalPlace().getName());
		assertEquals(journeyProposalJpaEntity.getJourneyRequest().getArrivalPlace().getDepartment().getId(),
				journeyProposalOutput.getJourney().getArrivalPlace().getDepartmentId());
		assertEquals(journeyProposalJpaEntity.getJourneyRequest().getArrivalPlace().getLatitude(),
				journeyProposalOutput.getJourney().getArrivalPlace().getLatitude());
		assertEquals(journeyProposalJpaEntity.getJourneyRequest().getArrivalPlace().getLongitude(),
				journeyProposalOutput.getJourney().getArrivalPlace().getLongitude());

		assertEquals(journeyProposalJpaEntity.getJourneyRequest().getEngineType().getId(),
				journeyProposalOutput.getJourney().getEngineType().getId());
		assertEquals(journeyProposalJpaEntity.getJourneyRequest().getEngineType().getName("en_US"),
				journeyProposalOutput.getJourney().getEngineType().getName());
		assertEquals(journeyProposalJpaEntity.getJourneyRequest().getEngineType().getCode().name(),
				journeyProposalOutput.getJourney().getEngineType().getCode());

		assertEquals(journeyProposalJpaEntity.getJourneyRequest().getClient().getId(),
				journeyProposalOutput.getJourney().getClient().getOauthId());
		assertEquals(journeyProposalJpaEntity.getJourneyRequest().getClient().getFirstname(),
				journeyProposalOutput.getJourney().getClient().getFirstname());
		assertEquals(journeyProposalJpaEntity.getJourneyRequest().getClient().getProfileImage().getId(),
				journeyProposalOutput.getJourney().getClient().getImageId());
		assertEquals(journeyProposalJpaEntity.getJourneyRequest().getClient().getProfileImage().getHash(),
				journeyProposalOutput.getJourney().getClient().getImageHash());

	}
}
