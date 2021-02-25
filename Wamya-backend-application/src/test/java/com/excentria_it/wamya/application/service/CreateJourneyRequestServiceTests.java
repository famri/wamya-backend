package com.excentria_it.wamya.application.service;

import static com.excentria_it.wamya.test.data.common.JourneyRequestTestData.*;
import static com.excentria_it.wamya.test.data.common.JourneyTravelInfoTestData.*;
import static com.excentria_it.wamya.test.data.common.UserAccountTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.in.CreateJourneyRequestUseCase.CreateJourneyRequestCommand;
import com.excentria_it.wamya.application.port.in.CreateJourneyRequestUseCase.CreateJourneyRequestCommand.CreateJourneyRequestCommandBuilder;
import com.excentria_it.wamya.application.port.out.CreateJourneyRequestPort;
import com.excentria_it.wamya.application.port.out.LoadPlaceGeoCoordinatesPort;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.common.exception.UserMobileNumberValidationException;
import com.excentria_it.wamya.domain.CreateJourneyRequestDto;
import com.excentria_it.wamya.domain.GeoCoordinates;
import com.excentria_it.wamya.domain.JourneyTravelInfo;
import com.excentria_it.wamya.domain.PlaceType;
import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ExtendWith(MockitoExtension.class)
public class CreateJourneyRequestServiceTests {

	@Mock
	private CreateJourneyRequestPort createJourneyRequestPort;
	@Mock
	private LoadUserAccountPort loadUserAccountPort;
	@Mock
	private JourneyTravelInfoService journeyTravelInfoService;
	@Mock
	private LoadPlaceGeoCoordinatesPort loadPlaceGeoCoordinatesPort;
	@Spy
	@InjectMocks
	private CreateJourneyRequestService createJourneyRequestsService;

	@Test
	void givenCreateJourneyRequestCommandAndUserEmail_WhenCreateJourneyRequest_ThenSucceed() {
		// given
		CreateJourneyRequestCommandBuilder commandBuilder = defaultCreateJourneyRequestCommandBuilder();
		CreateJourneyRequestCommand command = commandBuilder.build();

		ArgumentCaptor<CreateJourneyRequestDto> journeyRequestCaptor = ArgumentCaptor
				.forClass(CreateJourneyRequestDto.class);
		UserAccount userAccount = alreadyValidatedEmailUserAccount();
		given(loadUserAccountPort.loadUserAccountByEmail(any(String.class))).willReturn(Optional.of(userAccount));

		Optional<JourneyTravelInfo> jti = Optional.of(defaultJourneyTravelInfo());
		given(journeyTravelInfoService.loadTravelInfo(any(Long.class), any(PlaceType.class), any(Long.class),
				any(PlaceType.class))).willReturn(jti);
		given(loadPlaceGeoCoordinatesPort.loadPlaceGeoCoordinates(any(Long.class), any(PlaceType.class)))
				.willReturn(Optional.of(new GeoCoordinates(new BigDecimal(36.8015), new BigDecimal(10.1788))));

		// when
		createJourneyRequestsService.createJourneyRequest(command, TestConstants.DEFAULT_EMAIL, "en_US");

		// then
		then(createJourneyRequestPort).should(times(1)).createJourneyRequest(journeyRequestCaptor.capture(),
				eq(TestConstants.DEFAULT_EMAIL), eq("en_US"));

		assertThat(journeyRequestCaptor.getValue().getDeparturePlace().getId())
				.isEqualTo(command.getDeparturePlaceId());

		assertThat(journeyRequestCaptor.getValue().getDeparturePlace().getType().name())
				.isEqualTo(command.getDeparturePlaceType().toUpperCase());

		assertThat(journeyRequestCaptor.getValue().getArrivalPlace().getId()).isEqualTo(command.getArrivalPlaceId());

		assertThat(journeyRequestCaptor.getValue().getArrivalPlace().getType().name())
				.isEqualTo(command.getArrivalPlaceType().toUpperCase());

		assertThat(journeyRequestCaptor.getValue().getDateTime())
				.isEqualTo(command.getDateTime().withZoneSameInstant(ZoneOffset.UTC).toInstant());

		assertThat(journeyRequestCaptor.getValue().getEngineType().getId()).isEqualTo(command.getEngineTypeId());

		assertThat(journeyRequestCaptor.getValue().getDistance()).isEqualTo(jti.get().getDistance());

		assertThat(journeyRequestCaptor.getValue().getHours()).isEqualTo(jti.get().getHours());

		assertThat(journeyRequestCaptor.getValue().getMinutes()).isEqualTo(jti.get().getMinutes());

		assertThat(journeyRequestCaptor.getValue().getWorkers()).isEqualTo(command.getWorkers());

		assertThat(journeyRequestCaptor.getValue().getDescription()).isEqualTo(command.getDescription());

	}

	@Test
	void givenCreateJourneyRequestCommandAndGeoPlaceDepartureAndArrivalPlaces_WhenCreateJourneyRequest_ThenSucceed() {
		// given
		CreateJourneyRequestCommandBuilder commandBuilder = defaultCreateJourneyRequestCommandBuilder();
		CreateJourneyRequestCommand command = commandBuilder.build();

		ArgumentCaptor<CreateJourneyRequestDto> journeyRequestCaptor = ArgumentCaptor
				.forClass(CreateJourneyRequestDto.class);
		UserAccount userAccount = alreadyValidatedEmailUserAccount();
		given(loadUserAccountPort.loadUserAccountByEmail(any(String.class))).willReturn(Optional.of(userAccount));

		Optional<JourneyTravelInfo> jti = Optional.of(defaultJourneyTravelInfo());
		given(journeyTravelInfoService.loadTravelInfo(any(Long.class), any(PlaceType.class), any(Long.class),
				any(PlaceType.class))).willReturn(jti);
//		given(loadPlaceGeoCoordinatesPort.loadPlaceGeoCoordinates(any(Long.class), any(PlaceType.class)))
//				.willReturn(Optional.of(new GeoCoordinates(new BigDecimal(36.8015), new BigDecimal(10.1788))));
		command.setDeparturePlaceType("geo-place");
		command.setArrivalPlaceType("geo-place");
		// when
		createJourneyRequestsService.createJourneyRequest(command, TestConstants.DEFAULT_EMAIL, "en_US");

		// then
		then(createJourneyRequestPort).should(times(1)).createJourneyRequest(journeyRequestCaptor.capture(),
				eq(TestConstants.DEFAULT_EMAIL), eq("en_US"));

		assertThat(journeyRequestCaptor.getValue().getDeparturePlace().getId())
				.isEqualTo(command.getDeparturePlaceId());

		assertThat(journeyRequestCaptor.getValue().getDeparturePlace().getType().name())
				.isEqualTo(command.getDeparturePlaceType().toUpperCase().replace("-", "_"));

		assertThat(journeyRequestCaptor.getValue().getArrivalPlace().getId()).isEqualTo(command.getArrivalPlaceId());

		assertThat(journeyRequestCaptor.getValue().getArrivalPlace().getType().name())
				.isEqualTo(command.getArrivalPlaceType().toUpperCase().replace("-", "_"));

		assertThat(journeyRequestCaptor.getValue().getDateTime())
				.isEqualTo(command.getDateTime().withZoneSameInstant(ZoneOffset.UTC).toInstant());

		assertThat(journeyRequestCaptor.getValue().getEngineType().getId()).isEqualTo(command.getEngineTypeId());

		assertThat(journeyRequestCaptor.getValue().getDistance()).isEqualTo(jti.get().getDistance());

		assertThat(journeyRequestCaptor.getValue().getHours()).isEqualTo(jti.get().getHours());

		assertThat(journeyRequestCaptor.getValue().getMinutes()).isEqualTo(jti.get().getMinutes());

		assertThat(journeyRequestCaptor.getValue().getWorkers()).isEqualTo(command.getWorkers());

		assertThat(journeyRequestCaptor.getValue().getDescription()).isEqualTo(command.getDescription());

	}

	@Test
	void givenCreateJourneyRequestCommandAndInexistentUserEmail_WhenCreateJourneyRequest_ThenThrowUserAccountNotFoundException() {
		// given
		CreateJourneyRequestCommandBuilder commandBuilder = defaultCreateJourneyRequestCommandBuilder();
		CreateJourneyRequestCommand command = commandBuilder.build();

		given(loadUserAccountPort.loadUserAccountByEmail(any(String.class))).willReturn(Optional.ofNullable(null));
		// when
		assertThrows(UserAccountNotFoundException.class,
				() -> createJourneyRequestsService.createJourneyRequest(command, TestConstants.DEFAULT_EMAIL, "en_US"));

		// then
		then(createJourneyRequestPort).should(never()).createJourneyRequest(any(CreateJourneyRequestDto.class),
				any(String.class), any(String.class));

	}

	@Test
	void givenCreateJourneyRequestCommandAndUserEmailAndNotValidatedUserMobileNumber_WhenCreateJourneyRequest_ThenThrowUserMobileNumberValidationException() {
		// given
		CreateJourneyRequestCommandBuilder commandBuilder = defaultCreateJourneyRequestCommandBuilder();
		CreateJourneyRequestCommand command = commandBuilder.build();

		UserAccount userAccount = notYetValidatedMobileNumberUserAccount();

		given(loadUserAccountPort.loadUserAccountByEmail(any(String.class))).willReturn(Optional.of(userAccount));
		// when
		assertThrows(UserMobileNumberValidationException.class,
				() -> createJourneyRequestsService.createJourneyRequest(command, TestConstants.DEFAULT_EMAIL, "en_US"));

		// then
		then(createJourneyRequestPort).should(never()).createJourneyRequest(any(CreateJourneyRequestDto.class),
				any(String.class), any(String.class));

	}

	@Test
	void givenCreateJourneyRequestCommandAndUserMobileNumber_WhenCreateJourneyRequest_ThenSucceed() {
		// given
		CreateJourneyRequestCommandBuilder commandBuilder = defaultCreateJourneyRequestCommandBuilder();
		CreateJourneyRequestCommand command = commandBuilder.build();

		ArgumentCaptor<CreateJourneyRequestDto> journeyRequestCaptor = ArgumentCaptor
				.forClass(CreateJourneyRequestDto.class);
		UserAccount userAccount = alreadyValidatedEmailUserAccount();
		given(loadUserAccountPort.loadUserAccountByIccAndMobileNumber(any(String.class), any(String.class)))
				.willReturn(Optional.of(userAccount));
		Optional<JourneyTravelInfo> jti = Optional.of(defaultJourneyTravelInfo());
		given(journeyTravelInfoService.loadTravelInfo(any(Long.class), any(PlaceType.class), any(Long.class),
				any(PlaceType.class))).willReturn(jti);
		given(loadPlaceGeoCoordinatesPort.loadPlaceGeoCoordinates(any(Long.class), any(PlaceType.class)))
				.willReturn(Optional.of(new GeoCoordinates(new BigDecimal(36.8015), new BigDecimal(10.1788))));

		// when
		createJourneyRequestsService.createJourneyRequest(command, TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME,
				"en_US");

		// then
		then(createJourneyRequestPort).should(times(1)).createJourneyRequest(journeyRequestCaptor.capture(),
				eq(TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME), eq("en_US"));

		assertThat(journeyRequestCaptor.getValue().getDeparturePlace().getId())
				.isEqualTo(command.getDeparturePlaceId());
		assertThat(journeyRequestCaptor.getValue().getDeparturePlace().getType().name())
				.isEqualTo(command.getDeparturePlaceType().toUpperCase());

		assertThat(journeyRequestCaptor.getValue().getArrivalPlace().getType().name())
				.isEqualTo(command.getArrivalPlaceType().toUpperCase());

		assertThat(journeyRequestCaptor.getValue().getDateTime())
				.isEqualTo(command.getDateTime().withZoneSameInstant(ZoneOffset.UTC).toInstant());

		assertThat(journeyRequestCaptor.getValue().getEngineType().getId()).isEqualTo(command.getEngineTypeId());

		assertThat(journeyRequestCaptor.getValue().getDistance()).isEqualTo(jti.get().getDistance());

		assertThat(journeyRequestCaptor.getValue().getHours()).isEqualTo(jti.get().getHours());

		assertThat(journeyRequestCaptor.getValue().getMinutes()).isEqualTo(jti.get().getMinutes());

		assertThat(journeyRequestCaptor.getValue().getWorkers()).isEqualTo(command.getWorkers());

		assertThat(journeyRequestCaptor.getValue().getDescription()).isEqualTo(command.getDescription());

	}

	@Test
	void givenCreateJourneyRequestCommandAndUserMobileNumberAndNotValidatedUserMobileNumber_WhenCreateJourneyRequest_ThenThrowUserMobileNumberValidationException() {
		// given
		CreateJourneyRequestCommandBuilder commandBuilder = defaultCreateJourneyRequestCommandBuilder();
		CreateJourneyRequestCommand command = commandBuilder.build();

		UserAccount userAccount = notYetValidatedMobileNumberUserAccount();

		given(loadUserAccountPort.loadUserAccountByIccAndMobileNumber(any(String.class), any(String.class)))
				.willReturn(Optional.of(userAccount));
		// when
		assertThrows(UserMobileNumberValidationException.class, () -> createJourneyRequestsService
				.createJourneyRequest(command, TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME, "en_US"));

		// then
		then(createJourneyRequestPort).should(never()).createJourneyRequest(any(CreateJourneyRequestDto.class),
				any(String.class), any(String.class));

	}

	@Test
	void givenCreateJourneyRequestCommandAndNotFoundInDbJourneyTravelInfoAndEmptyJourneyTrvelInfo_WhenCreateJourneyRequest_ThenSucceed() {
		// given
		CreateJourneyRequestCommandBuilder commandBuilder = defaultCreateJourneyRequestCommandBuilder();
		CreateJourneyRequestCommand command = commandBuilder.build();

		ArgumentCaptor<CreateJourneyRequestDto> journeyRequestCaptor = ArgumentCaptor
				.forClass(CreateJourneyRequestDto.class);
		UserAccount userAccount = alreadyValidatedEmailUserAccount();
		given(loadUserAccountPort.loadUserAccountByIccAndMobileNumber(any(String.class), any(String.class)))
				.willReturn(Optional.of(userAccount));

		given(journeyTravelInfoService.loadTravelInfo(any(Long.class), any(PlaceType.class), any(Long.class),
				any(PlaceType.class))).willReturn(Optional.empty());
		given(loadPlaceGeoCoordinatesPort.loadPlaceGeoCoordinates(any(Long.class), any(PlaceType.class)))
				.willReturn(Optional.of(new GeoCoordinates(new BigDecimal(36.8015), new BigDecimal(10.1788))));
		// when
		createJourneyRequestsService.createJourneyRequest(command, TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME,
				"en_US");

		// then
		then(createJourneyRequestPort).should(times(1)).createJourneyRequest(journeyRequestCaptor.capture(),
				eq(TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME), eq("en_US"));

		assertThat(journeyRequestCaptor.getValue().getDeparturePlace().getId())
				.isEqualTo(command.getDeparturePlaceId());
		assertThat(journeyRequestCaptor.getValue().getDeparturePlace().getType().name())
				.isEqualTo(command.getDeparturePlaceType().toUpperCase());

		assertThat(journeyRequestCaptor.getValue().getArrivalPlace().getType().name())
				.isEqualTo(command.getArrivalPlaceType().toUpperCase());

		assertThat(journeyRequestCaptor.getValue().getDateTime())
				.isEqualTo(command.getDateTime().withZoneSameInstant(ZoneOffset.UTC).toInstant());

		assertThat(journeyRequestCaptor.getValue().getEngineType().getId()).isEqualTo(command.getEngineTypeId());

		assertThat(journeyRequestCaptor.getValue().getDistance()).isNull();

		assertThat(journeyRequestCaptor.getValue().getHours()).isNull();

		assertThat(journeyRequestCaptor.getValue().getMinutes()).isNull();

		assertThat(journeyRequestCaptor.getValue().getWorkers()).isEqualTo(command.getWorkers());

		assertThat(journeyRequestCaptor.getValue().getDescription()).isEqualTo(command.getDescription());
	}
}