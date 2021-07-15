package com.excentria_it.wamya.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.in.CreateJourneyRequestUseCase.CreateJourneyRequestCommand;
import com.excentria_it.wamya.application.port.out.LoadJourneyRequestPort;
import com.excentria_it.wamya.application.port.out.LoadPlaceGeoCoordinatesPort;
import com.excentria_it.wamya.application.port.out.UpdateJourneyRequestPort;
import com.excentria_it.wamya.application.utils.DateTimeHelper;
import com.excentria_it.wamya.common.exception.JourneyRequestNotFoundException;
import com.excentria_it.wamya.common.exception.JourneyRequestUpdateException;
import com.excentria_it.wamya.domain.ClientJourneyRequestDto;
import com.excentria_it.wamya.domain.GeoCoordinates;
import com.excentria_it.wamya.domain.JourneyRequestInputOutput;
import com.excentria_it.wamya.domain.JourneyTravelInfo;
import com.excentria_it.wamya.domain.PlaceType;
import com.excentria_it.wamya.test.data.common.JourneyRequestTestData;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ExtendWith(MockitoExtension.class)
public class UpdateJourneyRequestServiceTests {
	@Mock
	private LoadJourneyRequestPort loadJourneyRequestPort;
	@Mock
	private UpdateJourneyRequestPort updateJourneyRequestPort;
	@Mock
	private JourneyTravelInfoService journeyTravelInfoService;
	@Mock
	private LoadPlaceGeoCoordinatesPort loadPlaceGeoCoordinatesPort;
	@Mock
	private DateTimeHelper dateTimeHelper;

	@InjectMocks
	private UpdateJourneyRequestService updateJourneyRequestService;

	@Test
	void givenJourneyRequestNotFoundByIdAndClientEmail_whenUpdateJourneyRequest_thenThrowJourneyRequestNotFoundException() {
		// given
		given(loadJourneyRequestPort.loadJourneyRequestByIdAndClientEmail(any(Long.class), any(String.class),
				any(String.class))).willReturn(Optional.empty());
		CreateJourneyRequestCommand command = JourneyRequestTestData.defaultCreateJourneyRequestCommandBuilder()
				.build();
		// when

		// then
		assertThrows(JourneyRequestNotFoundException.class, () -> updateJourneyRequestService.updateJourneyRequest(1L,
				command, TestConstants.DEFAULT_EMAIL, "fr_FR"));
	}

	@Test
	void givenJourneyRequestExistsByIdAndClientEmail_andOffersHaveBeenMade_whenUpdateJourneyRequest_thenThrowJourneyRequestUpdateException() {
		// given
		ClientJourneyRequestDto clientJourneyRequestDto = JourneyRequestTestData.defaultClientJourneyRequestDto();
		given(loadJourneyRequestPort.loadJourneyRequestByIdAndClientEmail(any(Long.class), any(String.class),
				any(String.class))).willReturn(Optional.of(clientJourneyRequestDto));
		CreateJourneyRequestCommand command = JourneyRequestTestData.defaultCreateJourneyRequestCommandBuilder()
				.build();
		// when

		// then
		assertThrows(JourneyRequestUpdateException.class, () -> updateJourneyRequestService.updateJourneyRequest(1L,
				command, TestConstants.DEFAULT_EMAIL, "fr_FR"));
	}

	@Test
	void givenJourneyRequestExistsByIdAndClientEmail_andNoOffersHaveBeenMade_andNoChangeFromUpdateCommand_whenUpdateJourneyRequest_thenDonotUpdateJourneyRequest() {
		// given
		ClientJourneyRequestDto clientJourneyRequestDto = JourneyRequestTestData
				.defaultClientJourneyRequestDtoWithNoProposals();
		given(loadJourneyRequestPort.loadJourneyRequestByIdAndClientEmail(any(Long.class), any(String.class),
				any(String.class))).willReturn(Optional.of(clientJourneyRequestDto));
		given(dateTimeHelper.findUserZoneId(any(String.class))).willReturn(ZoneOffset.UTC);
		given(dateTimeHelper.userLocalToSystemDateTime(any(LocalDateTime.class), any(ZoneId.class)))
				.willReturn(clientJourneyRequestDto.getDateTime());

		CreateJourneyRequestCommand command = JourneyRequestTestData.defaultCreateJourneyRequestCommandBuilder()
				.departurePlaceId(clientJourneyRequestDto.getDeparturePlace().getId())
				.departurePlaceType(clientJourneyRequestDto.getDeparturePlace().getType())
				.arrivalPlaceId(clientJourneyRequestDto.getArrivalPlace().getId())
				.arrivalPlaceType(clientJourneyRequestDto.getArrivalPlace().getType())
				.dateTime(LocalDateTime.ofInstant(clientJourneyRequestDto.getDateTime(), ZoneOffset.UTC))
				.engineTypeId(clientJourneyRequestDto.getEngineType().getId())
				.workers(clientJourneyRequestDto.getWorkers()).description(clientJourneyRequestDto.getDescription())
				.build();
		// when
		updateJourneyRequestService.updateJourneyRequest(1L, command, TestConstants.DEFAULT_EMAIL, "fr_FR");
		// then
		then(updateJourneyRequestPort).should(never()).updateJourneyRequest(any(JourneyRequestInputOutput.class));
	}

	@Test
	void givenJourneyRequestExistsByIdAndClientEmail_andNoOffersHaveBeenMade_andChangeMadeUpdateCommand_andNoGeoCoordinates_whenUpdateJourneyRequest_thenUpdateJourneyRequest() {
		// given
		ClientJourneyRequestDto clientJourneyRequestDto = JourneyRequestTestData
				.defaultClientJourneyRequestDtoWithNoProposals();
		given(loadJourneyRequestPort.loadJourneyRequestByIdAndClientEmail(any(Long.class), any(String.class),
				any(String.class))).willReturn(Optional.of(clientJourneyRequestDto));
		given(dateTimeHelper.findUserZoneId(any(String.class))).willReturn(ZoneOffset.UTC);
		given(dateTimeHelper.userLocalToSystemDateTime(any(LocalDateTime.class), any(ZoneId.class)))
				.willReturn(clientJourneyRequestDto.getDateTime().plusSeconds(3600));

		CreateJourneyRequestCommand command = JourneyRequestTestData.defaultCreateJourneyRequestCommandBuilder()
				.departurePlaceId(clientJourneyRequestDto.getDeparturePlace().getId() + 1)
				.departurePlaceType(PlaceType.LOCALITY.toString())
				.arrivalPlaceId(clientJourneyRequestDto.getArrivalPlace().getId() + 1)
				.arrivalPlaceType(PlaceType.LOCALITY.toString())
				.dateTime(LocalDateTime.ofInstant(clientJourneyRequestDto.getDateTime().plusSeconds(3600),
						ZoneOffset.UTC))
				.engineTypeId(clientJourneyRequestDto.getEngineType().getId() + 1)
				.workers(clientJourneyRequestDto.getWorkers() + 1)
				.description(clientJourneyRequestDto.getDescription() + " some other description").build();

//		JourneyTravelInfo jti = new JourneyTravelInfo(200, 1, 30);
//
//		given(journeyTravelInfoService.loadTravelInfo(command.getDeparturePlaceId(), PlaceType.LOCALITY,
//				command.getArrivalPlaceId(), PlaceType.LOCALITY)).willReturn(Optional.of(jti));
//		given(loadPlaceGeoCoordinatesPort.loadPlaceGeoCoordinates(command.getDeparturePlaceId(), PlaceType.LOCALITY))
//				.willReturn(Optional.of(new GeoCoordinates(BigDecimal.valueOf(10.1111), BigDecimal.valueOf(34.1111))));
//		given(loadPlaceGeoCoordinatesPort.loadPlaceGeoCoordinates(command.getArrivalPlaceId(), PlaceType.LOCALITY))
//				.willReturn(Optional.of(new GeoCoordinates(BigDecimal.valueOf(10.1212), BigDecimal.valueOf(34.1212))));
		// when
		updateJourneyRequestService.updateJourneyRequest(1L, command, TestConstants.DEFAULT_EMAIL, "fr_FR");
		// then
		ArgumentCaptor<JourneyRequestInputOutput> argumentCaptor = ArgumentCaptor
				.forClass(JourneyRequestInputOutput.class);

		then(updateJourneyRequestPort).should(times(1)).updateJourneyRequest(argumentCaptor.capture());

		assertEquals(clientJourneyRequestDto.getDeparturePlace().getId() + 1,
				argumentCaptor.getValue().getDeparturePlace().getId());

		assertEquals(PlaceType.LOCALITY, argumentCaptor.getValue().getDeparturePlace().getType());

		assertEquals(clientJourneyRequestDto.getArrivalPlace().getId() + 1,
				argumentCaptor.getValue().getArrivalPlace().getId());

		assertEquals(PlaceType.LOCALITY, argumentCaptor.getValue().getArrivalPlace().getType());

		assertEquals(clientJourneyRequestDto.getDateTime().plusSeconds(3600), argumentCaptor.getValue().getDateTime());

		assertEquals(clientJourneyRequestDto.getEngineType().getId() + 1,
				argumentCaptor.getValue().getEngineType().getId());
		assertEquals(clientJourneyRequestDto.getWorkers() + 1, argumentCaptor.getValue().getWorkers());
		assertEquals(clientJourneyRequestDto.getDescription() + " some other description",
				argumentCaptor.getValue().getDescription());

	}

	@Test
	void givenJourneyRequestExistsByIdAndClientEmail_andNoOffersHaveBeenMade_andChangeMadeUpdateCommand_whenUpdateJourneyRequest_thenUpdateJourneyRequest() {
		// given
		ClientJourneyRequestDto clientJourneyRequestDto = JourneyRequestTestData
				.defaultClientJourneyRequestDtoWithNoProposals();
		given(loadJourneyRequestPort.loadJourneyRequestByIdAndClientEmail(any(Long.class), any(String.class),
				any(String.class))).willReturn(Optional.of(clientJourneyRequestDto));
		given(dateTimeHelper.findUserZoneId(any(String.class))).willReturn(ZoneOffset.UTC);
		given(dateTimeHelper.userLocalToSystemDateTime(any(LocalDateTime.class), any(ZoneId.class)))
				.willReturn(clientJourneyRequestDto.getDateTime().plusSeconds(3600));

		CreateJourneyRequestCommand command = JourneyRequestTestData.defaultCreateJourneyRequestCommandBuilder()
				.departurePlaceId(clientJourneyRequestDto.getDeparturePlace().getId() + 1)
				.departurePlaceType(PlaceType.LOCALITY.toString())
				.arrivalPlaceId(clientJourneyRequestDto.getArrivalPlace().getId() + 1)
				.arrivalPlaceType(PlaceType.LOCALITY.toString())
				.dateTime(LocalDateTime.ofInstant(clientJourneyRequestDto.getDateTime().plusSeconds(3600),
						ZoneOffset.UTC))
				.engineTypeId(clientJourneyRequestDto.getEngineType().getId() + 1)
				.workers(clientJourneyRequestDto.getWorkers() + 1)
				.description(clientJourneyRequestDto.getDescription() + " some other description").build();

		JourneyTravelInfo jti = new JourneyTravelInfo(200, 1, 30);

		given(journeyTravelInfoService.loadTravelInfo(command.getDeparturePlaceId(), PlaceType.LOCALITY,
				command.getArrivalPlaceId(), PlaceType.LOCALITY)).willReturn(Optional.of(jti));
		given(loadPlaceGeoCoordinatesPort.loadPlaceGeoCoordinates(command.getDeparturePlaceId(), PlaceType.LOCALITY))
				.willReturn(Optional.of(new GeoCoordinates(BigDecimal.valueOf(10.1111), BigDecimal.valueOf(34.1111))));
		given(loadPlaceGeoCoordinatesPort.loadPlaceGeoCoordinates(command.getArrivalPlaceId(), PlaceType.LOCALITY))
				.willReturn(Optional.of(new GeoCoordinates(BigDecimal.valueOf(10.1212), BigDecimal.valueOf(34.1212))));
		// when
		updateJourneyRequestService.updateJourneyRequest(1L, command, TestConstants.DEFAULT_EMAIL, "fr_FR");
		// then
		ArgumentCaptor<JourneyRequestInputOutput> argumentCaptor = ArgumentCaptor
				.forClass(JourneyRequestInputOutput.class);

		then(updateJourneyRequestPort).should(times(1)).updateJourneyRequest(argumentCaptor.capture());

		assertEquals(clientJourneyRequestDto.getDeparturePlace().getId() + 1,
				argumentCaptor.getValue().getDeparturePlace().getId());

		assertEquals(PlaceType.LOCALITY, argumentCaptor.getValue().getDeparturePlace().getType());

		assertEquals(clientJourneyRequestDto.getArrivalPlace().getId() + 1,
				argumentCaptor.getValue().getArrivalPlace().getId());

		assertEquals(PlaceType.LOCALITY, argumentCaptor.getValue().getArrivalPlace().getType());

		assertEquals(clientJourneyRequestDto.getDateTime().plusSeconds(3600), argumentCaptor.getValue().getDateTime());

		assertEquals(clientJourneyRequestDto.getEngineType().getId() + 1,
				argumentCaptor.getValue().getEngineType().getId());
		assertEquals(clientJourneyRequestDto.getWorkers() + 1, argumentCaptor.getValue().getWorkers());
		assertEquals(clientJourneyRequestDto.getDescription() + " some other description",
				argumentCaptor.getValue().getDescription());
		assertEquals(jti.getDistance(), argumentCaptor.getValue().getDistance());
		assertEquals(jti.getHours(), argumentCaptor.getValue().getHours());
		assertEquals(jti.getMinutes(), argumentCaptor.getValue().getMinutes());

	}
}
