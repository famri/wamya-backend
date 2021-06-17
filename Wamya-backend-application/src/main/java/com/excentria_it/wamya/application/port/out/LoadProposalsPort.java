package com.excentria_it.wamya.application.port.out;

import java.util.Optional;
import java.util.Set;

import com.excentria_it.wamya.common.domain.StatusCode;
import com.excentria_it.wamya.domain.JourneyProposalDto;
import com.excentria_it.wamya.domain.JourneyRequestProposals;
import com.excentria_it.wamya.domain.LoadJourneyProposalsCriteria;
import com.excentria_it.wamya.domain.TransporterNotificationInfo;

public interface LoadProposalsPort {
	JourneyRequestProposals loadJourneyProposals(LoadJourneyProposalsCriteria criteria, String locale);

	Optional<JourneyProposalDto> loadJourneyProposalByIdAndJourneyRequestId(Long proposalId, Long journeyRequestId,
			String locale);

	boolean isExistentJourneyProposalByIdAndJourneyRequestIdAndStatusCode(Long proposalId, Long journeyRequestId,
			StatusCode submitted);

	Set<TransporterNotificationInfo> loadTransportersNotificationInfo(Long journeyRequestId);

}
