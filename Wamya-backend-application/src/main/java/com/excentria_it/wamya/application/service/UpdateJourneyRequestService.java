package com.excentria_it.wamya.application.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.CreateJourneyRequestUseCase.CreateJourneyRequestCommand;
import com.excentria_it.wamya.application.port.in.UpdateJourneyRequestUseCase;
import com.excentria_it.wamya.application.port.out.LoadJourneyRequestPort;
import com.excentria_it.wamya.application.port.out.LoadPlaceGeoCoordinatesPort;
import com.excentria_it.wamya.application.port.out.UpdateJourneyRequestPort;
import com.excentria_it.wamya.application.utils.DateTimeHelper;
import com.excentria_it.wamya.application.utils.PlaceUtils;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.exception.JourneyRequestNotFoundException;
import com.excentria_it.wamya.common.exception.JourneyRequestUpdateException;
import com.excentria_it.wamya.domain.ClientJourneyRequestDtoOutput;
import com.excentria_it.wamya.domain.GeoCoordinates;
import com.excentria_it.wamya.domain.JourneyRequestInputOutput;
import com.excentria_it.wamya.domain.JourneyRequestInputOutput.EngineType;
import com.excentria_it.wamya.domain.JourneyRequestInputOutput.JourneyRequestInputOutputBuilder;
import com.excentria_it.wamya.domain.JourneyRequestInputOutput.Place;
import com.excentria_it.wamya.domain.JourneyTravelInfo;
import com.excentria_it.wamya.domain.PlaceType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@UseCase
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UpdateJourneyRequestService implements UpdateJourneyRequestUseCase {

	private final LoadJourneyRequestPort loadJourneyRequestPort;

	private final UpdateJourneyRequestPort updateJourneyRequestPort;

	private final JourneyTravelInfoService journeyTravelInfoService;

	private final LoadPlaceGeoCoordinatesPort loadPlaceGeoCoordinatesPort;

	private final DateTimeHelper dateTimeHelper;

	@Override
	public void updateJourneyRequest(Long journeyRequestId, CreateJourneyRequestCommand command, String username,
			String locale) {
		boolean shouldUpdateJourneyRequest = false;
		boolean updateJourneyTravelInfo = false;

		ClientJourneyRequestDtoOutput clientJourneyRequest = loadJourneyRequestPort
				.loadJourneyRequestByIdAndClientEmail(journeyRequestId, username, locale)
				.orElseThrow(() -> new JourneyRequestNotFoundException(
						String.format("Journey request not found by ID: %d", journeyRequestId)));

		if (clientJourneyRequest.getProposalsCount() > 0) {
			throw new JourneyRequestUpdateException(String.format(
					"Update cannot be accepted. Offers have been submitted for journey request with ID %s.",
					clientJourneyRequest.getId()));
		}

		JourneyRequestInputOutputBuilder journeyRequestBuilder = JourneyRequestInputOutput.builder();

		journeyRequestBuilder.id(journeyRequestId);

		PlaceType departurePlaceType = PlaceUtils
				.placeTypeStringToEnum(clientJourneyRequest.getDeparturePlace().getType());
		PlaceType arrivalPlaceType = PlaceUtils.placeTypeStringToEnum(clientJourneyRequest.getArrivalPlace().getType());

		if (!clientJourneyRequest.getDeparturePlace().getType().equals(command.getDeparturePlaceType())
				|| !clientJourneyRequest.getDeparturePlace().getId().equals(command.getDeparturePlaceId())) {
			updateJourneyTravelInfo = true;
			shouldUpdateJourneyRequest = true;
			departurePlaceType = PlaceUtils.placeTypeStringToEnum(command.getDeparturePlaceType());
			BigDecimal departurePlaceLatitude, departurePlaceLongitude;
			Optional<GeoCoordinates> departureGeoCoordinatesOptional = loadPlaceGeoCoordinatesPort
					.loadPlaceGeoCoordinates(command.getDeparturePlaceId(), departurePlaceType);

			if (departureGeoCoordinatesOptional.isPresent()) {
				departurePlaceLatitude = departureGeoCoordinatesOptional.get().getLatitude();
				departurePlaceLongitude = departureGeoCoordinatesOptional.get().getLongitude();
			} else {
				log.error(String.format("Unable to find geo-coordinates for place id: %d and place type: %s",
						command.getDeparturePlaceId(), departurePlaceType));
				departurePlaceLatitude = null;
				departurePlaceLongitude = null;
			}

			journeyRequestBuilder.departurePlace(new Place(command.getDeparturePlaceId(), departurePlaceType,
					departurePlaceLatitude, departurePlaceLongitude));
		}

		if (!clientJourneyRequest.getArrivalPlace().getType().equals(command.getArrivalPlaceType())
				|| !clientJourneyRequest.getArrivalPlace().getId().equals(command.getArrivalPlaceId())) {
			updateJourneyTravelInfo = true;
			shouldUpdateJourneyRequest = true;
			arrivalPlaceType = PlaceUtils.placeTypeStringToEnum(command.getArrivalPlaceType());
			BigDecimal arrivalPlaceLatitude, arrivalPlaceLongitude;
			Optional<GeoCoordinates> arrivalGeoCoordinatesOptional = loadPlaceGeoCoordinatesPort
					.loadPlaceGeoCoordinates(command.getArrivalPlaceId(), arrivalPlaceType);

			if (arrivalGeoCoordinatesOptional.isPresent()) {
				arrivalPlaceLatitude = arrivalGeoCoordinatesOptional.get().getLatitude();
				arrivalPlaceLongitude = arrivalGeoCoordinatesOptional.get().getLongitude();
			} else {
				log.error(String.format("Unable to find geo-coordinates for place id: %d and place type: %s",
						command.getDeparturePlaceId(), arrivalPlaceType));
				arrivalPlaceLatitude = null;
				arrivalPlaceLongitude = null;
			}

			journeyRequestBuilder.arrivalPlace(new Place(command.getArrivalPlaceId(), arrivalPlaceType,
					arrivalPlaceLatitude, arrivalPlaceLongitude));
		}

		ZoneId userZoneId = dateTimeHelper.findUserZoneId(username);
		Instant journeyDateTime = dateTimeHelper.userLocalToSystemDateTime(command.getDateTime(), userZoneId);
		if (!clientJourneyRequest.getDateTime().equals(journeyDateTime)) {
			shouldUpdateJourneyRequest = true;
			journeyRequestBuilder.dateTime(journeyDateTime);
		}

		if (!clientJourneyRequest.getEngineType().getId().equals(command.getEngineTypeId())) {
			shouldUpdateJourneyRequest = true;
			journeyRequestBuilder.engineType(new EngineType(command.getEngineTypeId(), null));
		}

		if (!clientJourneyRequest.getWorkers().equals(command.getWorkers())) {
			shouldUpdateJourneyRequest = true;
			journeyRequestBuilder.workers(command.getWorkers());
		}

		if (!clientJourneyRequest.getDescription().equals(command.getDescription())) {
			shouldUpdateJourneyRequest = true;
			journeyRequestBuilder.description(command.getDescription());
		}

		if (updateJourneyTravelInfo) {
			Optional<JourneyTravelInfo> journeyTravelInfo = journeyTravelInfoService.loadTravelInfo(
					command.getDeparturePlaceId(), departurePlaceType, command.getArrivalPlaceId(), arrivalPlaceType);

			if (journeyTravelInfo.isPresent()) {
				journeyRequestBuilder.distance(journeyTravelInfo.get().getDistance());
				journeyRequestBuilder.hours(journeyTravelInfo.get().getHours());
				journeyRequestBuilder.minutes(journeyTravelInfo.get().getMinutes());
			} else {
				log.error(String.format(
						"Unable to load travel info of journey from departure (%d, %s) to arrival (%d, %s)",
						command.getDeparturePlaceId(), departurePlaceType.name(), command.getArrivalPlaceId(),
						arrivalPlaceType.name()));

			}
		}

		if (shouldUpdateJourneyRequest || updateJourneyTravelInfo) {
			updateJourneyRequestPort.updateJourneyRequest(journeyRequestBuilder.build());
		}
	}

}
