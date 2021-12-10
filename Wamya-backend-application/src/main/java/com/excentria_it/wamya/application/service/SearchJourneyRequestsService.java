package com.excentria_it.wamya.application.service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.SearchJourneyRequestsUseCase;
import com.excentria_it.wamya.application.port.out.SearchJourneyRequestsPort;
import com.excentria_it.wamya.application.utils.DateTimeHelper;
import com.excentria_it.wamya.application.utils.DocumentUrlResolver;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.domain.JourneyRequestSearchDto;
import com.excentria_it.wamya.domain.JourneyRequestSearchDto.Client;
import com.excentria_it.wamya.domain.JourneyRequestSearchDto.EngineType;
import com.excentria_it.wamya.domain.JourneyRequestSearchDto.Place;
import com.excentria_it.wamya.domain.JourneyRequestSearchOutput;
import com.excentria_it.wamya.domain.JourneyRequestsSearchOutputResult;
import com.excentria_it.wamya.domain.JourneyRequestsSearchResult;
import com.excentria_it.wamya.domain.SearchJourneyRequestsInput;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class SearchJourneyRequestsService implements SearchJourneyRequestsUseCase {

	private final SearchJourneyRequestsPort searchJourneyRequestsPort;

	private final DateTimeHelper dateTimeHelper;

	private final DocumentUrlResolver documentUrlResolver;

	@Override
	public JourneyRequestsSearchResult searchJourneyRequests(SearchJourneyRequestsCommand command, String username,
			String locale) {

		ZoneId userZoneId = dateTimeHelper.findUserZoneId(username);

		Instant startInstant = dateTimeHelper.userLocalToSystemDateTime(command.getStartDateTime(), userZoneId);
		Instant endInstant = dateTimeHelper.userLocalToSystemDateTime(command.getEndDateTime(), userZoneId);

		SearchJourneyRequestsInput searchCriteria = new SearchJourneyRequestsInput(
				command.getDeparturePlaceDepartmentId(), command.getArrivalPlaceDepartmentIds(), startInstant,
				endInstant, command.getEngineTypes(), command.getPageNumber(), command.getPageSize(),
				command.getSortingCriterion(), locale);

		JourneyRequestsSearchOutputResult searchOutput = searchJourneyRequestsPort
				.searchJourneyRequests(searchCriteria);

		return new JourneyRequestsSearchResult(searchOutput.getTotalPages(), searchOutput.getTotalElements(),
				searchOutput.getPageNumber(), searchOutput.getPageSize(), searchOutput.isHasNext(),
				searchOutput.getContent().stream().map(j -> mapToJourneyRequestSearchDto(j, userZoneId))
						.collect(Collectors.toList()));
	}

	protected JourneyRequestSearchDto mapToJourneyRequestSearchDto(JourneyRequestSearchOutput jrso, ZoneId userZoneId) {

		return JourneyRequestSearchDto.builder().id(jrso.getId())
				.departurePlace(new Place(jrso.getDeparturePlace().getId(), jrso.getDeparturePlace().getType(),
						jrso.getDeparturePlace().getName(), jrso.getDeparturePlace().getLatitude(),
						jrso.getDeparturePlace().getLongitude(), jrso.getDeparturePlace().getDepartmentId()))
				.arrivalPlace(new Place(jrso.getArrivalPlace().getId(), jrso.getArrivalPlace().getType(),
						jrso.getArrivalPlace().getName(), jrso.getArrivalPlace().getLatitude(),
						jrso.getArrivalPlace().getLongitude(), jrso.getArrivalPlace().getDepartmentId()))
				.engineType(new EngineType(jrso.getEngineType().getId(), jrso.getEngineType().getName(),
						jrso.getEngineType().getCode()))
				.distance(jrso.getDistance()).hours(jrso.getHours()).minutes(jrso.getMinutes())
				.dateTime(dateTimeHelper.systemToUserLocalDateTime(jrso.getDateTime(), userZoneId))
				.workers(jrso.getWorkers()).description(jrso.getDescription())
				.client(new Client(jrso.getClient().getId(), jrso.getClient().getFirstname(),
						documentUrlResolver.resolveUrl(jrso.getClient().getImageId(), jrso.getClient().getImageHash())))
				.minPrice(jrso.getMinPrice()).build();

	}

}
