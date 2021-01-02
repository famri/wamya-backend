package com.excentria_it.wamya.application.service;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.LoadClientJourneyRequestsUseCase;
import com.excentria_it.wamya.application.port.out.LoadClientJourneyRequestsPort;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.domain.ClientJourneyRequests;
import com.excentria_it.wamya.domain.LoadClientJourneyRequestsCriteria;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class LoadJourneyRequestsService implements LoadClientJourneyRequestsUseCase {

	private final LoadClientJourneyRequestsPort loadClientJourneyRequestsPort;

	@Override
	public ClientJourneyRequests loadJourneyRequests(LoadJourneyRequestsCommand command, String locale) {

		LoadClientJourneyRequestsCriteria loadingCriteria = LoadClientJourneyRequestsCriteria.builder()
				.clientUsername(command.getClientUsername()).periodCriterion(command.getPeriodCriterion())
				.sortingCriterion(command.getSortingCriterion()).pageNumber(command.getPageNumber())
				.pageSize(command.getPageSize()).locale(locale).build();

		return loadClientJourneyRequestsPort.loadClientJourneyRequests(loadingCriteria);

	}

}
