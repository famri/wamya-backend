package com.excentria_it.wamya.adapter.persistence.mapper;

import static com.excentria_it.wamya.test.data.common.JourneyProposalJpaEntityTestData.*;
import static com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData.*;
import static com.excentria_it.wamya.test.data.common.VehiculeJpaEntityTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.VehiculeJpaEntity;
import com.excentria_it.wamya.domain.JourneyProposalDto;

public class JourneyProposalMapperTests {

	private JourneyProposalMapper journeyProposalMapper = new JourneyProposalMapper();
	private static final Double JOURNEY_PRICE = 250.0;

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
		
		JourneyProposalDto journeyProposalDto = journeyProposalMapper.mapToDomainEntity(journeyProposalJpaEntity);
		
		assertEquals(journeyProposalJpaEntity.getId(), journeyProposalDto.getId());
		assertEquals(journeyProposalJpaEntity.getPrice(), journeyProposalDto.getPrice());

		assertEquals(journeyProposalJpaEntity.getTransporter().getId(), journeyProposalDto.getTransporterDto().getId());
		assertEquals(journeyProposalJpaEntity.getTransporter().getFirstname(),
				journeyProposalDto.getTransporterDto().getFirstname());
		assertEquals(journeyProposalJpaEntity.getTransporter().getGlobalRating(),
				journeyProposalDto.getTransporterDto().getGlobalRating());
		assertEquals(journeyProposalJpaEntity.getTransporter().getPhotoUrl(),
				journeyProposalDto.getTransporterDto().getPhotoUrl());

		assertEquals(journeyProposalJpaEntity.getVehicule().getId(), journeyProposalDto.getVehiculeDto().getId());
		assertEquals(journeyProposalJpaEntity.getVehicule().getModel().getConstructor().getName(),
				journeyProposalDto.getVehiculeDto().getConstructor());
		assertEquals(journeyProposalJpaEntity.getVehicule().getModel().getName(),
				journeyProposalDto.getVehiculeDto().getModel());
		assertEquals(journeyProposalJpaEntity.getVehicule().getPhotoUrl(),
				journeyProposalDto.getVehiculeDto().getPhotoUrl());
	}

	@Test
	void testMapNullToDomainEntity() {

		JourneyProposalDto journeyProposalDto = journeyProposalMapper.mapToDomainEntity(null);

		assertNull(journeyProposalDto);
	}
}
