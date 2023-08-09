package com.excentria_it.wamya.test.data.common;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterRatingRequestRecordJpaEntity;
import com.excentria_it.wamya.domain.TransporterRatingRequestStatus;

public class TransporterRatingRequestJpaTestData {
	public static TransporterRatingRequestRecordJpaEntity defaultTransporterRatingRequestRecordJpaEntity() {

		JourneyRequestJpaEntity journeyRequest = JourneyRequestJpaTestData
				.defaultExistentJourneyRequestJpaEntityWithAcceptedProposal();
		ClientJpaEntity client = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();
		TransporterJpaEntity transporter = UserAccountJpaEntityTestData.defaultExistentTransporterJpaEntity();

		return new TransporterRatingRequestRecordJpaEntity(1L, journeyRequest, transporter, client, "SOME_HASH", 0,
				TransporterRatingRequestStatus.SAVED);

	}
}
