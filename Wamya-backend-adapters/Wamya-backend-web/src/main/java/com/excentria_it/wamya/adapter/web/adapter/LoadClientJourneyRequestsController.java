package com.excentria_it.wamya.adapter.web.adapter;

import java.util.Locale;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.adapter.web.utils.ValidationHelper;
import com.excentria_it.wamya.application.port.in.LoadClientJourneyRequestsUseCase;
import com.excentria_it.wamya.application.port.in.LoadClientJourneyRequestsUseCase.LoadJourneyRequestCommand;
import com.excentria_it.wamya.application.port.in.LoadClientJourneyRequestsUseCase.LoadJourneyRequestsCommand;
import com.excentria_it.wamya.common.PeriodCriterion;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.utils.LocaleUtils;
import com.excentria_it.wamya.common.utils.ParameterUtils;
import com.excentria_it.wamya.domain.ClientJourneyRequestDto;
import com.excentria_it.wamya.domain.ClientJourneyRequests;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoadClientJourneyRequestsController {

	private final LoadClientJourneyRequestsUseCase loadJourneyRequestUseCase;

	private final ValidationHelper validationHelper;

	@GetMapping(path = "/me/journey-requests")
	@ResponseStatus(HttpStatus.OK)
	public ClientJourneyRequests loadClientJourneyRequests(
			@RequestParam(name = "period", defaultValue = "m1") Optional<String> period,
			@RequestParam(name = "page", defaultValue = "0") Integer pageNumber,
			@RequestParam(name = "size", defaultValue = "25") Integer pageSize,
			@RequestParam(name = "sort") Optional<String> sort,
			final @AuthenticationPrincipal JwtAuthenticationToken principal, Locale locale) {

		SortCriterion sortingCriterion = ParameterUtils.parameterToSortCriterion(sort, "date-time,desc");
		PeriodCriterion periodCriterion = ParameterUtils.parameterToPeriodCriterion(period, "m1");

		LoadJourneyRequestsCommand command = LoadJourneyRequestsCommand.builder().clientUsername(principal.getName())
				.pageNumber(pageNumber).pageSize(pageSize).sortingCriterion(sortingCriterion)
				.periodCriterion(periodCriterion).build();

		validationHelper.validateInput(command);

		Locale supportedLocale = LocaleUtils.getSupporedLocale(locale);

		return loadJourneyRequestUseCase.loadJourneyRequests(command, supportedLocale.toString());

	}

	@GetMapping(path = "/me/journey-requests/{journeyRequestId}")
	@ResponseStatus(HttpStatus.OK)
	public ClientJourneyRequestDto loadClientJourneyRequest(
			@PathVariable(name = "journeyRequestId") Long journeyRequestId,
			final @AuthenticationPrincipal JwtAuthenticationToken principal, Locale locale) {

		LoadJourneyRequestCommand command = LoadJourneyRequestCommand.builder().clientUsername(principal.getName())
				.journeyRequestId(journeyRequestId).build();

		validationHelper.validateInput(command);

		Locale supportedLocale = LocaleUtils.getSupporedLocale(locale);

		return loadJourneyRequestUseCase.loadJourneyRequest(command, supportedLocale.toString());

	}
}
