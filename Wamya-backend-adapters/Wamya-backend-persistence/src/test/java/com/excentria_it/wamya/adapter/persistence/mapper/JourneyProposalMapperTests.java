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
import com.excentria_it.wamya.test.data.common.DocumentJpaTestData;
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
	void testMapToDomainEntity() {

		JourneyProposalJpaEntity journeyProposalJpaEntity = defaultJourneyProposalJpaEntity();

		JourneyProposalDto journeyProposalDto = journeyProposalMapper.mapToDomainEntity(journeyProposalJpaEntity,
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

		JourneyProposalDto journeyProposalDto = journeyProposalMapper.mapToDomainEntity(journeyProposalJpaEntity,
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

		JourneyProposalDto journeyProposalDto = journeyProposalMapper.mapToDomainEntity(null, "en_US");

		assertNull(journeyProposalDto);
	}

	private String getVehiculeImageUrl(VehiculeJpaEntity vehicule) {
		return (vehicule.getImage() != null)
				? documentUrlResolver.resolveUrl(vehicule.getImage().getId(), vehicule.getImage().getHash())
				: documentUrlResolver.resolveUrl(vehicule.getType().getImage().getId(),
						vehicule.getType().getImage().getHash());
	}
}
