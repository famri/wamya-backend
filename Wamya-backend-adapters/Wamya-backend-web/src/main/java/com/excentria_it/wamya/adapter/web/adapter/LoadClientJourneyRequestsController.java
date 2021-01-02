package com.excentria_it.wamya.adapter.web.adapter;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.LoadClientJourneyRequestsUseCase;
import com.excentria_it.wamya.application.port.in.LoadClientJourneyRequestsUseCase.LoadJourneyRequestsCommand;
import com.excentria_it.wamya.common.PeriodCriterion;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.utils.LocaleUtils;
import com.excentria_it.wamya.common.utils.ParameterUtils;
import com.excentria_it.wamya.domain.ClientJourneyRequests;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoadClientJourneyRequestsController {

	private final LoadClientJourneyRequestsUseCase loadJourneyRequestUseCase;

	private final LocalValidatorFactoryBean localValidatorFactoryBean;

	@GetMapping(path = "/me/journey-requests")
	@ResponseStatus(HttpStatus.OK)
	public ClientJourneyRequests loadClientJourneyRequests(@RequestParam(name = "period") Optional<String> period,
			@RequestParam(name = "page", defaultValue = "1") Integer pageNumber,
			@RequestParam(name = "size", defaultValue = "25") Integer pageSize,
			@RequestParam(name = "sort") Optional<String> sort,
			final @AuthenticationPrincipal JwtAuthenticationToken principal, Locale locale) {

		SortCriterion sortingCriterion = ParameterUtils.parameterToSortCriterion(sort, "date-time,desc");
		PeriodCriterion periodCriterion = ParameterUtils.parameterToPeriodCriterion(period, "m1");

		LoadJourneyRequestsCommand command = LoadJourneyRequestsCommand.builder().clientUsername(principal.getName())
				.pageNumber(pageNumber).pageSize(pageSize).sortingCriterion(sortingCriterion)
				.periodCriterion(periodCriterion).build();

		validateInput(command);

		Locale supportedLocale = LocaleUtils.getSupporedLocale(locale);

		return loadJourneyRequestUseCase.loadJourneyRequests(command, supportedLocale.toString());

	}

	protected void validateInput(LoadJourneyRequestsCommand command) {
		Set<ConstraintViolation<LoadJourneyRequestsCommand>> errors = localValidatorFactoryBean.getValidator()
				.validate(command);
		if (!errors.isEmpty()) {
			throw new ConstraintViolationException(errors);
		}
	}
}
