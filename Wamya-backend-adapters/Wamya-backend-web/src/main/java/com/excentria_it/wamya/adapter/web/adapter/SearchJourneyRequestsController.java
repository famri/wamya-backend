package com.excentria_it.wamya.adapter.web.adapter;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.adapter.web.utils.ValidationHelper;
import com.excentria_it.wamya.application.port.in.SearchJourneyRequestsUseCase;
import com.excentria_it.wamya.application.port.in.SearchJourneyRequestsUseCase.SearchJourneyRequestsCommand;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.utils.LocaleUtils;
import com.excentria_it.wamya.common.utils.ParameterUtils;
import com.excentria_it.wamya.domain.JourneyRequestsSearchResult;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/journey-requests", produces = MediaType.APPLICATION_JSON_VALUE)
public class SearchJourneyRequestsController {

	private final SearchJourneyRequestsUseCase searchJourneyRequestsUseCase;

	private final ValidationHelper validationHelper;

	@GetMapping
	public JourneyRequestsSearchResult searchJourneyRequests(
			@RequestParam(name = "departure") Long departurePlaceRegionId,
			@RequestParam(name = "arrival") Set<Long> arrivalPlaceRegionIds,
			@RequestParam(name = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS") LocalDateTime startDateTime,
			@RequestParam(name = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS") LocalDateTime endDateTime,
			@RequestParam(name = "engine") Set<Long> engineTypeIds,
			@RequestParam(name = "page", defaultValue = "0") Integer pageNumber,
			@RequestParam(name = "size", defaultValue = "25") Integer pageSize,
			@RequestParam(name = "sort") Optional<String> sort,
			final @AuthenticationPrincipal JwtAuthenticationToken principal, Locale locale) {

		Locale supportedLocale = LocaleUtils.getSupporedLocale(locale);

		SortCriterion sortingCriterion = ParameterUtils.parameterToSortCriterion(sort, "date-time,desc");

		SearchJourneyRequestsCommand command = SearchJourneyRequestsCommand.builder()
				.departurePlaceDepartmentId(departurePlaceRegionId).arrivalPlaceDepartmentIds(arrivalPlaceRegionIds)
				.startDateTime(startDateTime).endDateTime(endDateTime).engineTypes(engineTypeIds).pageNumber(pageNumber)
				.pageSize(pageSize).sortingCriterion(sortingCriterion).build();

		validationHelper.validateInput(command);

		return searchJourneyRequestsUseCase.searchJourneyRequests(command, principal.getName(),
				supportedLocale.toString());

	}

}
