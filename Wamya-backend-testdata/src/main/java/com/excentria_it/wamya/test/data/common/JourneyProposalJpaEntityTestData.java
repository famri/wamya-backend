package com.excentria_it.wamya.test.data.common;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalJpaEntity.JourneyProposalJpaEntityBuilder;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalStatusJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalStatusJpaEntity.JourneyProposalStatusJpaEntityBuilder;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalStatusJpaEntity.JourneyProposalStatusCode;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedId;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedJourneyProposalStatusJpaEntity;

public class JourneyProposalJpaEntityTestData {

	public static JourneyProposalJpaEntityBuilder defaultJourneyProposalJpaEntityBuilder() {
		return JourneyProposalJpaEntity.builder().id(1L).price(250.0)
				.creationDateTime(LocalDateTime.now(ZoneOffset.UTC)).status(defaultJourneyProposalStatusJpaEntity())
				.transporter(UserAccountJpaEntityTestData.defaultExistentTransporterJpaEntity())
				.vehicule(VehiculeJpaEntityTestData.defaultVehiculeJpaEntity());
	}

	public static JourneyProposalJpaEntity defaultJourneyProposalJpaEntity() {
		return JourneyProposalJpaEntity.builder().id(1L).price(250.0)
				.creationDateTime(LocalDateTime.now(ZoneOffset.UTC)).status(defaultJourneyProposalStatusJpaEntity())
				.transporter(UserAccountJpaEntityTestData.defaultExistentTransporterJpaEntity())
				.vehicule(VehiculeJpaEntityTestData.defaultVehiculeJpaEntity()).build();
	}

	public static JourneyProposalStatusJpaEntity defaultJourneyProposalStatusJpaEntity() {

		LocalizedJourneyProposalStatusJpaEntity localizedJourneyProposalStatusJpaEntity = LocalizedJourneyProposalStatusJpaEntity
				.builder().value("submitted").localizedId(new LocalizedId(1L, "en_US")).build();

		return JourneyProposalStatusJpaEntity.builder().id(1L).code(JourneyProposalStatusCode.SUBMITTED)
				.description("Journey proposal was submitted to client. He should accept or reject the proposal.")
				.localizations(Map.of("en_US", localizedJourneyProposalStatusJpaEntity)).build();

	}
	
	public static JourneyProposalStatusJpaEntityBuilder defaultJourneyProposalStatusJpaEntityBuilder() {

		LocalizedJourneyProposalStatusJpaEntity localizedJourneyProposalStatusJpaEntity = LocalizedJourneyProposalStatusJpaEntity
				.builder().value("submitted").localizedId(new LocalizedId(1L, "en_US")).build();

		return JourneyProposalStatusJpaEntity.builder().id(1L).code(JourneyProposalStatusCode.SUBMITTED)
				.description("Journey proposal was submitted to client. He should accept or reject the proposal.")
				.localizations(Map.of("en_US", localizedJourneyProposalStatusJpaEntity));

	}
}
