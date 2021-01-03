package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.domain.JourneyRequestProposals;
import com.excentria_it.wamya.domain.LoadJourneyProposalsCriteria;

public interface LoadProposalsPort {
	JourneyRequestProposals loadJourneyProposals(LoadJourneyProposalsCriteria criteria);
}
