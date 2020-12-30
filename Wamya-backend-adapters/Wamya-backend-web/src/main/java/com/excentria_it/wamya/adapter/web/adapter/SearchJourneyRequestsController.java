package com.excentria_it.wamya.adapter.web.adapter;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.adapter.web.utils.LocaleUtils;
import com.excentria_it.wamya.application.port.in.SearchJourneyRequestsUseCase;
import com.excentria_it.wamya.application.port.in.SearchJourneyRequestsUseCase.SearchJourneyRequestsCommand;
import com.excentria_it.wamya.common.SortingCriterion;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.domain.JourneyRequestsSearchResult;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = "/journey-requests", produces = MediaType.APPLICATION_JSON_VALUE)
public class SearchJourneyRequestsController {

	private final SearchJourneyRequestsUseCase searchJourneyRequestsUseCase;
	
	private final LocalValidatorFactoryBean localValidatorFactoryBean;

	@GetMapping
	public JourneyRequestsSearchResult searchJourneyRequests(
			@RequestParam(name = "departure") String departurePlaceRegionId,
			@RequestParam(name = "arrival") Set<String> arrivalPlaceRegionIds,
			@RequestParam(name = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startDateTime,
			@RequestParam(name = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endDateTime,
			@RequestParam(name = "engine") Set<Long> engineTypeIds, @RequestParam(name = "page") Integer pageNumber,
			@RequestParam(name = "size") Integer pageSize, @RequestParam(name = "sort") Optional<String> sort,
			Locale locale) {

		Locale supportedLocale = LocaleUtils.getSupporedLocale(locale);

		SortingCriterion sortingCriterion = null;
		String sortStr = sort.orElse("min-price,desc");

		String field, direction;

		String[] sortFieldAndDirection = sortStr.split(",");

		if (sortFieldAndDirection.length == 2) {
			field = sortFieldAndDirection[0].trim();
			direction = sortFieldAndDirection[1].trim();

		} else {
			field = sortFieldAndDirection[0].trim();
			direction = "asc";

		}

		sortingCriterion = new SortingCriterion(field, direction);

		SearchJourneyRequestsCommand command = SearchJourneyRequestsCommand.builder()
				.departurePlaceRegionId(departurePlaceRegionId).arrivalPlaceRegionIds(arrivalPlaceRegionIds)
				.startDateTime(startDateTime).endDateTime(endDateTime).engineTypes(engineTypeIds).pageNumber(pageNumber)
				.pageSize(pageSize).sortingCriterion(sortingCriterion).build();

		validateInput(command);

		return searchJourneyRequestsUseCase.searchJourneyRequests(command, supportedLocale.toString());

	}

	protected void validateInput(SearchJourneyRequestsCommand command) {
		Set<ConstraintViolation<SearchJourneyRequestsCommand>> errors = localValidatorFactoryBean.getValidator()
				.validate(command);
		if (!errors.isEmpty()) {
			throw new ConstraintViolationException(errors);
		}
	}
}
