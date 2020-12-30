package com.excentria_it.wamya.test.data.common;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalJpaEntity;

public class JourneyProposalJpaEntityTestData {
	public static JourneyProposalJpaEntity defaultJourneyProposalJpaEntity() {
		return JourneyProposalJpaEntity.builder().id(1L).price(250.0)
				.creationDateTime(LocalDateTime.now(ZoneOffset.UTC))
				.transporter(UserAccountJpaEntityTestData.defaultExistentTransporterJpaEntity())
				.vehicule(VehiculeJpaEntityTestData.defaultVehiculeJpaEntity()).build();
	}
}
