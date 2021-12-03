package com.excentria_it.wamya.adapter.persistence.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.adapter.persistence.entity.TransporterRatingRequestRecordJpaEntity;
import com.excentria_it.wamya.application.utils.DocumentUrlResolver;
import com.excentria_it.wamya.domain.TransporterRatingRequestRecordOutput;
import com.excentria_it.wamya.test.data.common.TransporterRatingRequestJpaTestData;

public class TransporterRatingRequestRecordMapperTests {

	private DocumentUrlResolver documentUrlResolver = new DocumentUrlResolver();

	private TransporterRatingRequestRecordMapper transporterRatingDetailsMapper = new TransporterRatingRequestRecordMapper(
			documentUrlResolver);

	@BeforeEach
	void initDocumentUrlResolver() {
		documentUrlResolver.setServerBaseUrl("https://domain-name:port/wamya-backend");
	}

	@Test
	void testMapToTransporterRatingDetailsOutputFromNullTransporterRatingDetailsJpaEntity() {
		TransporterRatingRequestRecordOutput transporterRatingDetailsOutput = transporterRatingDetailsMapper
				.mapToDomainEntity(null, "fr_FR");
		assertEquals(null, transporterRatingDetailsOutput);
	}

	@Test
	void testMapToTransporterRatingDetailsOutputUsingNullLocale() {
		TransporterRatingRequestRecordJpaEntity trdje = TransporterRatingRequestJpaTestData
				.defaultTransporterRatingRequestRecordJpaEntity();

		TransporterRatingRequestRecordOutput transporterRatingDetailsOutput = transporterRatingDetailsMapper
				.mapToDomainEntity(trdje, null);
		assertEquals(null, transporterRatingDetailsOutput);
	}

	@Test
	void testMapToTransporterRatingDetailsOutputUsingEmptyLocale() {
		TransporterRatingRequestRecordJpaEntity trdje = TransporterRatingRequestJpaTestData
				.defaultTransporterRatingRequestRecordJpaEntity();
		TransporterRatingRequestRecordOutput transporterRatingDetailsOutput = transporterRatingDetailsMapper
				.mapToDomainEntity(trdje, "");
		assertEquals(null, transporterRatingDetailsOutput);
	}

	@Test
	void testMapToDomainEntity() {
		TransporterRatingRequestRecordJpaEntity trdje = TransporterRatingRequestJpaTestData
				.defaultTransporterRatingRequestRecordJpaEntity();

		TransporterRatingRequestRecordOutput result = transporterRatingDetailsMapper.mapToDomainEntity(trdje, "fr_FR");

		assertEquals(trdje.getJourenyRequest().getDeparturePlace().getName("fr_FR"),
				result.getJourneyRequest().getDeparturePlace().getName());
		assertEquals(trdje.getJourenyRequest().getArrivalPlace().getName("fr_FR"),
				result.getJourneyRequest().getArrivalPlace().getName());
		assertEquals(trdje.getJourenyRequest().getDateTime(), result.getJourneyRequest().getDateTime());

		assertEquals(trdje.getTransporter().getFirstname(), result.getTransporter().getFirstname());
		assertEquals(trdje.getTransporter().getGlobalRating(), result.getTransporter().getGlobalRating());
		assertEquals(documentUrlResolver.resolveUrl(trdje.getTransporter().getProfileImage().getId(),
				trdje.getTransporter().getProfileImage().getHash()), result.getTransporter().getPhotoUrl());

	}
}
