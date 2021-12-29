package com.excentria_it.wamya.adapter.web.adapter;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.adapter.web.utils.ValidationHelper;
import com.excentria_it.wamya.application.port.in.LoadTransporterProposalsUseCase;
import com.excentria_it.wamya.application.port.in.LoadTransporterProposalsUseCase.LoadTransporterProposalsCommand;
import com.excentria_it.wamya.common.PeriodCriterion;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.utils.LocaleUtils;
import com.excentria_it.wamya.common.utils.ParameterUtils;
import com.excentria_it.wamya.domain.JourneyProposalStatusCode;
import com.excentria_it.wamya.domain.TransporterProposals;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoadTransporterProposalsController {

	private final LoadTransporterProposalsUseCase loadTransporterProposalsUseCase;

	private final ValidationHelper validationHelper;

	@GetMapping(path = "/me/proposals")
	@ResponseStatus(HttpStatus.OK)
	public TransporterProposals loadTransporterProposals(

			@RequestParam(name = "page", defaultValue = "0") Integer pageNumber,
			@RequestParam(name = "size", defaultValue = "25") Integer pageSize,
			@RequestParam(name = "sort") Optional<String> sort,
			@RequestParam(name = "period", defaultValue = "w1") Optional<String> period,
			@RequestParam(name = "statuses") Set<JourneyProposalStatusCode> statusCodes,
			final @AuthenticationPrincipal JwtAuthenticationToken principal, Locale locale) {

		SortCriterion sortingCriterion = ParameterUtils.parameterToSortCriterion(sort, "date-time,desc");
		PeriodCriterion periodCriterion = ParameterUtils.parameterToPeriodCriterion(period, "w1");

		LoadTransporterProposalsCommand command = LoadTransporterProposalsCommand.builder()
				.transporterUsername(principal.getName()).pageNumber(pageNumber).pageSize(pageSize)
				.sortingCriterion(sortingCriterion).periodCriterion(periodCriterion).statusCodes(statusCodes).build();

		validationHelper.validateInput(command);

		Locale supportedLocale = LocaleUtils.getSupporedLocale(locale);

		return loadTransporterProposalsUseCase.loadProposals(command, supportedLocale.toString());

	}

}
