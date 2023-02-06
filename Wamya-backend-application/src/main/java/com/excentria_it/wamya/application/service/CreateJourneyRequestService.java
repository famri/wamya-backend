package com.excentria_it.wamya.application.service;

import com.excentria_it.wamya.application.port.in.CreateJourneyRequestUseCase;
import com.excentria_it.wamya.application.port.out.CreateJourneyRequestPort;
import com.excentria_it.wamya.application.port.out.LoadPlaceGeoCoordinatesPort;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.utils.DateTimeHelper;
import com.excentria_it.wamya.application.utils.PlaceUtils;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.domain.*;
import com.excentria_it.wamya.domain.CreateJourneyRequestDto.EngineTypeDto;
import com.excentria_it.wamya.domain.CreateJourneyRequestDto.PlaceDto;
import com.excentria_it.wamya.domain.JourneyRequestInputOutput.EngineType;
import com.excentria_it.wamya.domain.JourneyRequestInputOutput.Place;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@UseCase
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CreateJourneyRequestService implements CreateJourneyRequestUseCase {

    private final CreateJourneyRequestPort createJourneyRequestPort;

    private final LoadUserAccountPort loadUserAccountPort;

    private final JourneyTravelInfoService journeyTravelInfoService;

    private final LoadPlaceGeoCoordinatesPort loadPlaceGeoCoordinatesPort;

    private final DateTimeHelper dateTimeHelper;

    @Override
    public CreateJourneyRequestDto createJourneyRequest(CreateJourneyRequestCommand command, String subject,
                                                        String locale) {

        // checkUserMobileNumberIsVerified(username);

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
            throw new IllegalArgumentException(
                    String.format("Unknown departure place id %d", command.getDeparturePlaceId()));
        }

        Optional<GeoCoordinates> arrivalGeoCoordinatesOptional = loadPlaceGeoCoordinatesPort
                .loadPlaceGeoCoordinates(command.getArrivalPlaceId(), arrivalPlaceType);

        if (arrivalGeoCoordinatesOptional.isPresent()) {
            arrivalPlaceLatitude = arrivalGeoCoordinatesOptional.get().getLatitude();
            arrivalPlaceLongitude = arrivalGeoCoordinatesOptional.get().getLongitude();
        } else {
            log.error(String.format("Unable to find geo-coordinates for place id: %d and place type: %s",
                    command.getArrivalPlaceId(), arrivalPlaceType));
            throw new IllegalArgumentException(
                    String.format("Unknown arrival place id %d", command.getArrivalPlaceId()));
        }

        ZoneId userZoneId = dateTimeHelper.findUserZoneId(subject);
        Instant journeyDateTime = dateTimeHelper.userLocalToSystemDateTime(command.getDateTime(), userZoneId);

        JourneyRequestInputOutput journeyRequest = JourneyRequestInputOutput.builder()
                .departurePlace(new Place(command.getDeparturePlaceId(), departurePlaceType, departurePlaceLatitude,
                        departurePlaceLongitude))
                .arrivalPlace(new Place(command.getArrivalPlaceId(), arrivalPlaceType, arrivalPlaceLatitude,
                        arrivalPlaceLongitude))
                .dateTime(journeyDateTime).creationDateTime(Instant.now())
                .engineType(new EngineType(command.getEngineTypeId(), null)).workers(command.getWorkers())
                .description(command.getDescription()).build();

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

        JourneyRequestInputOutput createdJourneyRequest = createJourneyRequestPort.createJourneyRequest(journeyRequest,
                subject, locale);

        LocalDateTime creationLocalDateTime = dateTimeHelper
                .systemToUserLocalDateTime(createdJourneyRequest.getCreationDateTime(), userZoneId);

        return CreateJourneyRequestDto.builder().id(createdJourneyRequest.getId())
                .departurePlace(new PlaceDto(command.getDeparturePlaceId(), departurePlaceType, departurePlaceLatitude,
                        departurePlaceLongitude))
                .arrivalPlace(new PlaceDto(command.getArrivalPlaceId(), arrivalPlaceType, arrivalPlaceLatitude,
                        arrivalPlaceLongitude))
                .dateTime(command.getDateTime()).creationDateTime(creationLocalDateTime)
                .status(createdJourneyRequest.getStatus())
                .engineType(new EngineTypeDto(command.getEngineTypeId(), journeyRequest.getEngineType().getName()))
                .workers(command.getWorkers()).description(command.getDescription()).build();

    }

}
