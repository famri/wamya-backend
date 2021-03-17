package com.excentria_it.wamya.adapter.web.adapter;

import java.util.List;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.LoadProposalsUseCase;
import com.excentria_it.wamya.application.port.in.LoadProposalsUseCase.LoadProposalsCommand;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.domain.StatusCode;
import com.excentria_it.wamya.common.utils.LocaleUtils;
import com.excentria_it.wamya.common.utils.ParameterUtils;
import com.excentria_it.wamya.domain.JourneyRequestProposals;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = "/journey-requests", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoadProposalsController {

	private final LoadProposalsUseCase loadProposalsUseCase;

	private final LocalValidatorFactoryBean localValidatorFactoryBean;

	@GetMapping(path = "/{journeyRequestId}/proposals")
	@ResponseStatus(HttpStatus.OK)
	public JourneyRequestProposals loadJourneyRequestProposals(@PathVariable("journeyRequestId") Long journeyRequestId,
			@RequestParam(name = "page", defaultValue = "0") Integer pageNumber,
			@RequestParam(name = "size", defaultValue = "25") Integer pageSize,
			@RequestParam(name = "sort") Optional<String> sort, @RequestParam(name = "filter") Optional<String> filter,
			final @AuthenticationPrincipal JwtAuthenticationToken principal, Locale locale) {

		SortCriterion sortingCriterion = ParameterUtils.parameterToSortCriterion(sort, "price,asc");
		List<StatusCode> statusCodes = ParameterUtils.parseProposalStatusFilter(filter);

		LoadProposalsCommand command = LoadProposalsCommand.builder().clientUsername(principal.getName())
				.pageNumber(pageNumber).pageSize(pageSize).sortingCriterion(sortingCriterion)
				.journeyRequestId(journeyRequestId).statusCodes(statusCodes).build();

		validateInput(command);

		Locale supportedLocale = LocaleUtils.getSupporedLocale(locale);

		return loadProposalsUseCase.loadProposals(command, supportedLocale.toString());

	}

	protected void validateInput(LoadProposalsCommand command) {
		Set<ConstraintViolation<LoadProposalsCommand>> errors = localValidatorFactoryBean.getValidator()
				.validate(command);
		if (!errors.isEmpty()) {
			throw new ConstraintViolationException(errors);
		}
	}
}
