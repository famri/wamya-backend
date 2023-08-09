package com.excentria_it.wamya.application.service;

import static com.excentria_it.wamya.test.data.common.JourneyRequestTestData.*;
import static com.excentria_it.wamya.test.data.common.JourneyTravelInfoTestData.*;
import static com.excentria_it.wamya.test.data.common.UserAccountTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
import com.excentria_it.wamya.application.utils.DateTimeHelper;
import com.excentria_it.wamya.application.utils.PlaceUtils;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.common.exception.UserMobileNumberValidationException;
import com.excentria_it.wamya.domain.GeoCoordinates;
import com.excentria_it.wamya.domain.JourneyRequestInputOutput;
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
	@Mock
	private DateTimeHelper dateTimeHelper;
	@Spy
	@InjectMocks
	private CreateJourneyRequestService createJourneyRequestsService;

	@Test
	void givenCreateJourneyRequestCommandAndUserEmail_WhenCreateJourneyRequest_ThenSucceed() {
		// given
		CreateJourneyRequestCommandBuilder commandBuilder = defaultCreateJourneyRequestCommandBuilder();
		CreateJourneyRequestCommand command = commandBuilder.build();

		ArgumentCaptor<JourneyRequestInputOutput> journeyRequestCaptor = ArgumentCaptor
				.forClass(JourneyRequestInputOutput.class);
//		UserAccount userAccount = alreadyValidatedEmailUserAccount();
//		given(loadUserAccountPort.loadUserAccountByUsername(any(String.class))).willReturn(Optional.of(userAccount));

		Optional<JourneyTravelInfo> jti = Optional.of(defaultJourneyTravelInfo());
		given(journeyTravelInfoService.loadTravelInfo(any(Long.class), any(PlaceType.class), any(Long.class),
				any(PlaceType.class))).willReturn(jti);
		given(loadPlaceGeoCoordinatesPort.loadPlaceGeoCoordinates(any(Long.class), any(PlaceType.class)))
				.willReturn(Optional.of(new GeoCoordinates(new BigDecimal(36.8015), new BigDecimal(10.1788))));

		ZoneId userZoneId = ZoneId.of("Africa/Tunis");
		given(dateTimeHelper.findUserZoneId(any(String.class))).willReturn(userZoneId);
		Instant now = Instant.now();
		given(dateTimeHelper.systemToUserLocalDateTime(any(Instant.class), any(ZoneId.class)))
				.willReturn(now.atZone(userZoneId).toLocalDateTime());
		given(dateTimeHelper.userLocalToSystemDateTime(any(LocalDateTime.class), any(ZoneId.class)))
				.willReturn(command.getDateTime().atZone(userZoneId).toInstant());

		JourneyRequestInputOutput journeyRequest = defaultJourneyRequestInputOutputBuilder().creationDateTime(now)
				.build();
		given(createJourneyRequestPort.createJourneyRequest(any(JourneyRequestInputOutput.class), any(String.class),
				any(String.class))).willReturn(journeyRequest);

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
				.isEqualTo(command.getDateTime().atZone(userZoneId).toInstant());

		assertThat(journeyRequestCaptor.getValue().getEngineType().getId()).isEqualTo(command.getEngineTypeId());

		assertThat(journeyRequestCaptor.getValue().getDistance()).isEqualTo(jti.get().getDistance());

		assertThat(journeyRequestCaptor.getValue().getHours()).isEqualTo(jti.get().getHours());

		assertThat(journeyRequestCaptor.getValue().getMinutes()).isEqualTo(jti.get().getMinutes());

		assertThat(journeyRequestCaptor.getValue().getWorkers()).isEqualTo(command.getWorkers());

		assertThat(journeyRequestCaptor.getValue().getDescription()).isEqualTo(command.getDescription());

	}

	@Test
	void givenDepartureGeoPlaceCoordinatesNotFound_whenCreateJourneyRequest_thenThrowIllegalArgumentException() {
		// given
		given(loadPlaceGeoCoordinatesPort.loadPlaceGeoCoordinates(any(Long.class), any(PlaceType.class)))
				.willReturn(Optional.empty());

		CreateJourneyRequestCommand command = defaultCreateJourneyRequestCommandBuilder().build();
		// When

		// then
		assertThrows(IllegalArgumentException.class,
				() -> createJourneyRequestsService.createJourneyRequest(command, TestConstants.DEFAULT_EMAIL, "fr_FR"));

	}

	@Test
	void givenArrivalGeoPlaceCoordinatesNotFound_whenCreateJourneyRequest_thenThrowIllegalArgumentException() {
		// given

		CreateJourneyRequestCommand command = defaultCreateJourneyRequestCommandBuilder().build();
		given(loadPlaceGeoCoordinatesPort.loadPlaceGeoCoordinates(command.getDeparturePlaceId(),
				PlaceUtils.placeTypeStringToEnum(command.getDeparturePlaceType())))
						.willReturn(Optional.of(new GeoCoordinates(new BigDecimal(36.8015), new BigDecimal(10.1788))));
		given(loadPlaceGeoCoordinatesPort.loadPlaceGeoCoordinates(command.getArrivalPlaceId(),
				PlaceUtils.placeTypeStringToEnum(command.getArrivalPlaceType()))).willReturn(Optional.empty());
		// When

		// then
		assertThrows(IllegalArgumentException.class,
				() -> createJourneyRequestsService.createJourneyRequest(command, TestConstants.DEFAULT_EMAIL, "fr_FR"));

	}

	// @Test
	void givenCreateJourneyRequestCommandAndGeoPlaceDepartureAndArrivalPlaces_WhenCreateJourneyRequest_ThenSucceed() {
		// given
		CreateJourneyRequestCommandBuilder commandBuilder = defaultCreateJourneyRequestCommandBuilder();
		CreateJourneyRequestCommand command = commandBuilder.build();

		ArgumentCaptor<JourneyRequestInputOutput> journeyRequestCaptor = ArgumentCaptor
				.forClass(JourneyRequestInputOutput.class);
//		UserAccount userAccount = alreadyValidatedEmailUserAccount();
//		given(loadUserAccountPort.loadUserAccountByUsername(any(String.class))).willReturn(Optional.of(userAccount));

		Optional<JourneyTravelInfo> jti = Optional.of(defaultJourneyTravelInfo());
		given(journeyTravelInfoService.loadTravelInfo(any(Long.class), any(PlaceType.class), any(Long.class),
				any(PlaceType.class))).willReturn(jti);

		ZoneId userZoneId = ZoneId.of("Africa/Tunis");
		given(dateTimeHelper.findUserZoneId(any(String.class))).willReturn(userZoneId);
		Instant now = Instant.now();
		given(dateTimeHelper.systemToUserLocalDateTime(any(Instant.class), any(ZoneId.class)))
				.willReturn(now.atZone(userZoneId).toLocalDateTime());
		given(dateTimeHelper.userLocalToSystemDateTime(any(LocalDateTime.class), any(ZoneId.class)))
				.willReturn(command.getDateTime().atZone(userZoneId).toInstant());

		JourneyRequestInputOutput journeyRequest = defaultJourneyRequestInputOutputBuilder().creationDateTime(now)
				.build();
		given(createJourneyRequestPort.createJourneyRequest(any(JourneyRequestInputOutput.class), any(String.class),
				any(String.class))).willReturn(journeyRequest);

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
				.isEqualTo(command.getDateTime().atZone(userZoneId).toInstant());

		assertThat(journeyRequestCaptor.getValue().getEngineType().getId()).isEqualTo(command.getEngineTypeId());

		assertThat(journeyRequestCaptor.getValue().getDistance()).isEqualTo(jti.get().getDistance());

		assertThat(journeyRequestCaptor.getValue().getHours()).isEqualTo(jti.get().getHours());

		assertThat(journeyRequestCaptor.getValue().getMinutes()).isEqualTo(jti.get().getMinutes());

		assertThat(journeyRequestCaptor.getValue().getWorkers()).isEqualTo(command.getWorkers());

		assertThat(journeyRequestCaptor.getValue().getDescription()).isEqualTo(command.getDescription());

	}

	// @Test
	void givenCreateJourneyRequestCommandAndInexistentUserEmail_WhenCreateJourneyRequest_ThenThrowUserAccountNotFoundException() {
		// given
		CreateJourneyRequestCommandBuilder commandBuilder = defaultCreateJourneyRequestCommandBuilder();
		CreateJourneyRequestCommand command = commandBuilder.build();

		given(loadUserAccountPort.loadUserAccountBySubject(any(String.class))).willReturn(Optional.empty());
		// when
		assertThrows(UserAccountNotFoundException.class,
				() -> createJourneyRequestsService.createJourneyRequest(command, TestConstants.DEFAULT_EMAIL, "en_US"));

		// then
		then(createJourneyRequestPort).should(never()).createJourneyRequest(any(JourneyRequestInputOutput.class),
				any(String.class), any(String.class));

	}

	// @Test
	void givenCreateJourneyRequestCommandAndUserEmailAndNotValidatedUserMobileNumber_WhenCreateJourneyRequest_ThenThrowUserMobileNumberValidationException() {
		// given
		CreateJourneyRequestCommandBuilder commandBuilder = defaultCreateJourneyRequestCommandBuilder();
		CreateJourneyRequestCommand command = commandBuilder.build();

		UserAccount userAccount = notYetValidatedMobileNumberUserAccount();

		given(loadUserAccountPort.loadUserAccountBySubject(any(String.class))).willReturn(Optional.of(userAccount));
		// when
		assertThrows(UserMobileNumberValidationException.class,
				() -> createJourneyRequestsService.createJourneyRequest(command, TestConstants.DEFAULT_EMAIL, "en_US"));

		// then
		then(createJourneyRequestPort).should(never()).createJourneyRequest(any(JourneyRequestInputOutput.class),
				any(String.class), any(String.class));

	}

	@Test
	void givenCreateJourneyRequestCommandAndUserMobileNumber_WhenCreateJourneyRequest_ThenSucceed() {
		// given
		CreateJourneyRequestCommandBuilder commandBuilder = defaultCreateJourneyRequestCommandBuilder();
		CreateJourneyRequestCommand command = commandBuilder.build();

		ArgumentCaptor<JourneyRequestInputOutput> journeyRequestCaptor = ArgumentCaptor
				.forClass(JourneyRequestInputOutput.class);

//		UserAccount userAccount = alreadyValidatedEmailUserAccount();
//		given(loadUserAccountPort.loadUserAccountByUsername(any(String.class))).willReturn(Optional.of(userAccount));

		Optional<JourneyTravelInfo> jti = Optional.of(defaultJourneyTravelInfo());
		given(journeyTravelInfoService.loadTravelInfo(any(Long.class), any(PlaceType.class), any(Long.class),
				any(PlaceType.class))).willReturn(jti);
		given(loadPlaceGeoCoordinatesPort.loadPlaceGeoCoordinates(any(Long.class), any(PlaceType.class)))
				.willReturn(Optional.of(new GeoCoordinates(new BigDecimal(36.8015), new BigDecimal(10.1788))));
		ZoneId userZoneId = ZoneId.of("Africa/Tunis");
		given(dateTimeHelper.findUserZoneId(any(String.class))).willReturn(userZoneId);
		Instant now = Instant.now();
		given(dateTimeHelper.systemToUserLocalDateTime(any(Instant.class), any(ZoneId.class)))
				.willReturn(now.atZone(userZoneId).toLocalDateTime());
		given(dateTimeHelper.userLocalToSystemDateTime(any(LocalDateTime.class), any(ZoneId.class)))
				.willReturn(command.getDateTime().atZone(userZoneId).toInstant());

		JourneyRequestInputOutput journeyRequest = defaultJourneyRequestInputOutputBuilder().creationDateTime(now)
				.build();
		given(createJourneyRequestPort.createJourneyRequest(any(JourneyRequestInputOutput.class), any(String.class),
				any(String.class))).willReturn(journeyRequest);

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
				.isEqualTo(command.getDateTime().atZone(userZoneId).toInstant());

		assertThat(journeyRequestCaptor.getValue().getEngineType().getId()).isEqualTo(command.getEngineTypeId());

		assertThat(journeyRequestCaptor.getValue().getDistance()).isEqualTo(jti.get().getDistance());

		assertThat(journeyRequestCaptor.getValue().getHours()).isEqualTo(jti.get().getHours());

		assertThat(journeyRequestCaptor.getValue().getMinutes()).isEqualTo(jti.get().getMinutes());

		assertThat(journeyRequestCaptor.getValue().getWorkers()).isEqualTo(command.getWorkers());

		assertThat(journeyRequestCaptor.getValue().getDescription()).isEqualTo(command.getDescription());

	}

	// @Test
	void givenCreateJourneyRequestCommandAndUserMobileNumberAndNotValidatedUserMobileNumber_WhenCreateJourneyRequest_ThenThrowUserMobileNumberValidationException() {
		// given
		CreateJourneyRequestCommandBuilder commandBuilder = defaultCreateJourneyRequestCommandBuilder();
		CreateJourneyRequestCommand command = commandBuilder.build();

		UserAccount userAccount = notYetValidatedMobileNumberUserAccount();

		given(loadUserAccountPort.loadUserAccountBySubject(any(String.class))).willReturn(Optional.of(userAccount));
		// when
		assertThrows(UserMobileNumberValidationException.class, () -> createJourneyRequestsService
				.createJourneyRequest(command, TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME, "en_US"));

		// then
		then(createJourneyRequestPort).should(never()).createJourneyRequest(any(JourneyRequestInputOutput.class),
				any(String.class), any(String.class));

	}

	@Test
	void givenCreateJourneyRequestCommandAndNotFoundInDbJourneyTravelInfoAndEmptyJourneyTrvelInfo_WhenCreateJourneyRequest_ThenSucceed() {
		// given
		CreateJourneyRequestCommandBuilder commandBuilder = defaultCreateJourneyRequestCommandBuilder();
		CreateJourneyRequestCommand command = commandBuilder.build();

		ArgumentCaptor<JourneyRequestInputOutput> journeyRequestCaptor = ArgumentCaptor
				.forClass(JourneyRequestInputOutput.class);
//		UserAccount userAccount = alreadyValidatedEmailUserAccount();
//		given(loadUserAccountPort.loadUserAccountByUsername(any(String.class))).willReturn(Optional.of(userAccount));

		given(journeyTravelInfoService.loadTravelInfo(any(Long.class), any(PlaceType.class), any(Long.class),
				any(PlaceType.class))).willReturn(Optional.empty());
		given(loadPlaceGeoCoordinatesPort.loadPlaceGeoCoordinates(any(Long.class), any(PlaceType.class)))
				.willReturn(Optional.of(new GeoCoordinates(new BigDecimal(36.8015), new BigDecimal(10.1788))));

		ZoneId userZoneId = ZoneId.of("Africa/Tunis");
		given(dateTimeHelper.findUserZoneId(any(String.class))).willReturn(userZoneId);
		Instant now = Instant.now();
		given(dateTimeHelper.systemToUserLocalDateTime(any(Instant.class), any(ZoneId.class)))
				.willReturn(now.atZone(userZoneId).toLocalDateTime());
		given(dateTimeHelper.userLocalToSystemDateTime(any(LocalDateTime.class), any(ZoneId.class)))
				.willReturn(command.getDateTime().atZone(userZoneId).toInstant());

		JourneyRequestInputOutput journeyRequest = defaultJourneyRequestInputOutputBuilder().creationDateTime(now)
				.build();
		given(createJourneyRequestPort.createJourneyRequest(any(JourneyRequestInputOutput.class), any(String.class),
				any(String.class))).willReturn(journeyRequest);
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
				.isEqualTo(command.getDateTime().atZone(userZoneId).toInstant());

		assertThat(journeyRequestCaptor.getValue().getEngineType().getId()).isEqualTo(command.getEngineTypeId());

		assertThat(journeyRequestCaptor.getValue().getDistance()).isNull();

		assertThat(journeyRequestCaptor.getValue().getHours()).isNull();

		assertThat(journeyRequestCaptor.getValue().getMinutes()).isNull();

		assertThat(journeyRequestCaptor.getValue().getWorkers()).isEqualTo(command.getWorkers());

		assertThat(journeyRequestCaptor.getValue().getDescription()).isEqualTo(command.getDescription());
	}
}