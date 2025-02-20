package com.excentria_it.wamya.application.task;

import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.scheduling.annotation.Scheduled;

import com.excentria_it.wamya.application.port.out.CreateTransporterRatingRequestPort;
import com.excentria_it.wamya.application.port.out.LoadJourneyRequestPort;
import com.excentria_it.wamya.application.port.out.UpdateJourneyRequestPort;
import com.excentria_it.wamya.application.props.CreateTransporterRatingRequestProperties;
import com.excentria_it.wamya.application.service.helper.HashGenerator;
import com.excentria_it.wamya.common.HashAlgorithm;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.domain.JourneyRequestStatusCode;
import com.excentria_it.wamya.domain.TransporterRatingRequestStatus;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class CreateTransporterRatingRequestsTask {

	private final LoadJourneyRequestPort loadJourneyRequestPort;
	private final UpdateJourneyRequestPort updateJourneyRequestPort;
	private final CreateTransporterRatingRequestPort createTransporterRatingRequestPort;
	private final HashGenerator hashGenerator;

	private final CreateTransporterRatingRequestProperties transporterRatingRequestProperties;

	@Scheduled(cron = "${app.rating.record.creation-cron-expression}")
	public void createTransporterRatingRequests() {

		Set<Long> fulfilledJourneysIds = loadJourneyRequestPort.loadJourneyRequestIdsByStatusCodeAndLimit(
				JourneyRequestStatusCode.FULFILLED, transporterRatingRequestProperties.getLimit());

		if (!fulfilledJourneysIds.isEmpty()) {
			createTransporterRatingRequestPort.createTransporterRatingRequests(fulfilledJourneysIds,
					hashGenerator.generateHashes(fulfilledJourneysIds, HashAlgorithm.SHA3_256));
			updateJourneyRequestPort.updateJourneyStatus(fulfilledJourneysIds, JourneyRequestStatusCode.ARCHIVED);
		}

	}
}