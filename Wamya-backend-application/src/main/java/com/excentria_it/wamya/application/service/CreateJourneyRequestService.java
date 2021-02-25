package com.excentria_it.wamya.application.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.CreateJourneyRequestUseCase;
import com.excentria_it.wamya.application.port.out.CreateJourneyRequestPort;
import com.excentria_it.wamya.application.port.out.LoadPlaceGeoCoordinatesPort;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.utils.PlaceUtils;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.common.exception.UserMobileNumberValidationException;
import com.excentria_it.wamya.domain.CreateJourneyRequestDto;
import com.excentria_it.wamya.domain.CreateJourneyRequestDto.EngineTypeDto;
import com.excentria_it.wamya.domain.CreateJourneyRequestDto.PlaceDto;
import com.excentria_it.wamya.domain.GeoCoordinates;
import com.excentria_it.wamya.domain.JourneyTravelInfo;
import com.excentria_it.wamya.domain.PlaceType;
import com.excentria_it.wamya.domain.UserAccount;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@UseCase
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CreateJourneyRequestService implements CreateJourneyRequestUseCase {

	private final CreateJourneyRequestPort createJourneyRequestPort;

	private final LoadUserAccountPort loadUserAccountPort;

	private final JourneyTravelInfoService journeyTravelInfoService;

	private final LoadPlaceGeoCoordinatesPort loadPlaceGeoCoordinatesPort;

	@Override
	public CreateJourneyRequestDto createJourneyRequest(CreateJourneyRequestCommand command, String username,
			String locale) {

		checkUserMobileNumberIsVerified(username);

		PlaceType departurePlaceType = PlaceUtils.placeTypeStringToEnum(command.getDeparturePlaceType());
		PlaceType arrivalPlaceType = PlaceUtils.placeTypeStringToEnum(command.getArrivalPlaceType());

		BigDecimal departurePlaceLatitude, departurePlaceLongitude, arrivalPlaceLatitude, arrivalPlaceLongitude;

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

		Optional<GeoCoordinates> arrivalGeoCoordinatesOptional = loadPlaceGeoCoordinatesPort
				.loadPlaceGeoCoordinates(command.getArrivalPlaceId(), arrivalPlaceType);
		if (arrivalGeoCoordinatesOptional.isPresent()) {
			arrivalPlaceLatitude = arrivalGeoCoordinatesOptional.get().getLatitude();
			arrivalPlaceLongitude = arrivalGeoCoordinatesOptional.get().getLongitude();
		} else {
			log.error(String.format("Unable to find geo-coordinates for place id: %d and place type: %s",
					command.getArrivalPlaceId(), arrivalPlaceType));
			arrivalPlaceLatitude = null;
			arrivalPlaceLongitude = null;
		}

		CreateJourneyRequestDto journeyRequest = CreateJourneyRequestDto.builder()
				.departurePlace(new PlaceDto(command.getDeparturePlaceId(), departurePlaceType, departurePlaceLatitude,
						departurePlaceLongitude))
				.arrivalPlace(new PlaceDto(command.getArrivalPlaceId(), arrivalPlaceType, arrivalPlaceLatitude,
						arrivalPlaceLongitude))
				.dateTime(command.getDateTime().withZoneSameInstant(ZoneOffset.UTC).toInstant())
				.creationDateTime(Instant.now()).engineType(new EngineTypeDto(command.getEngineTypeId(), null))
				.workers(command.getWorkers()).description(command.getDescription()).build();

		Optional<JourneyTravelInfo> journeyTravelInfo = journeyTravelInfoService.loadTravelInfo(
				command.getDeparturePlaceId(), departurePlaceType, command.getArrivalPlaceId(), arrivalPlaceType);

		if (journeyTravelInfo.isPresent()) {
			journeyRequest.setDistance(journeyTravelInfo.get().getDistance());
			journeyRequest.setHours(journeyTravelInfo.get().getHours());
			journeyRequest.setMinutes(journeyTravelInfo.get().getMinutes());
		} else {
			log.error(String.format("Unable to load travel info of journey from departure (%d, %s) to arrival (%d, %s)",
					command.getDeparturePlaceId(), departurePlaceType.name(), command.getArrivalPlaceId(),
					arrivalPlaceType.name()));

			journeyRequest.setDistance(null);
			journeyRequest.setHours(null);
			journeyRequest.setMinutes(null);

		}
		return createJourneyRequestPort.createJourneyRequest(journeyRequest, username, locale);
	}

	private void checkUserMobileNumberIsVerified(String username) {
		Optional<UserAccount> userAccountOptional = null;
		if (username.contains("@")) {
			userAccountOptional = loadUserAccountPort.loadUserAccountByEmail(username);
		} else {
			String[] mobileNumber = username.split("_");
			userAccountOptional = loadUserAccountPort.loadUserAccountByIccAndMobileNumber(mobileNumber[0],
					mobileNumber[1]);
		}

		if (userAccountOptional.isEmpty()) {
			throw new UserAccountNotFoundException("User " + username + " does not exist.");
		}

		UserAccount userAccount = userAccountOptional.get();

		if (!userAccount.getIsValidatedMobileNumber()) {
			throw new UserMobileNumberValidationException("User " + username + " mobile number is not yet validated.");
		}

	}
}
