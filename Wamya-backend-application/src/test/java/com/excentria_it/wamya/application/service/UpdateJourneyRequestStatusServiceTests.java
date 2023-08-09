package com.excentria_it.wamya.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.out.CancelJourneyRequestPort;
import com.excentria_it.wamya.application.port.out.LoadJourneyRequestPort;
import com.excentria_it.wamya.application.port.out.LoadPlaceNamesPort;
import com.excentria_it.wamya.application.port.out.LoadProposalsPort;
import com.excentria_it.wamya.application.port.out.AsynchronousMessagingPort;
import com.excentria_it.wamya.application.utils.DateTimeHelper;
import com.excentria_it.wamya.common.domain.PushMessage;
import com.excentria_it.wamya.common.exception.JourneyRequestNotFoundException;
import com.excentria_it.wamya.domain.ClientJourneyRequestDtoOutput;
import com.excentria_it.wamya.domain.JourneyRequestStatusCode;
import com.excentria_it.wamya.domain.PlaceType;
import com.excentria_it.wamya.domain.TransporterNotificationInfo;
import com.excentria_it.wamya.test.data.common.JourneyRequestTestData;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ExtendWith(MockitoExtension.class)
public class UpdateJourneyRequestStatusServiceTests {
	@Mock
	private LoadJourneyRequestPort loadJourneyRequestPort;
	@Mock
	private CancelJourneyRequestPort cancelJourneyRequestPort;
	@Mock
	private LoadProposalsPort loadProposalsPort;
	@Mock
	private AsynchronousMessagingPort messagingPort;
	@Mock
	private LoadPlaceNamesPort loadPlaceNamesPort;
	@Mock
	private DateTimeHelper dateTimeHelper;

	@InjectMocks
	private UpdateJourneyRequestStatusService updateJourneyRequestStatusService;

	@Test
	void givenJourneyRequestNotFoundByIdAndClientEmail_whenCancelJourneyRequest_thenThrowJourneyRequestNotFoundException() {
		// given
		given(loadJourneyRequestPort.loadJourneyRequestByIdAndClientSubject(any(Long.class), any(String.class),
				any(String.class))).willReturn(Optional.empty());
		// when
		// then
		assertThrows(JourneyRequestNotFoundException.class, () -> updateJourneyRequestStatusService.updateStatus(1L,
				TestConstants.DEFAULT_EMAIL, JourneyRequestStatusCode.CANCELED, "fr_FR"));
	}

	@Test
	void givenJourneyRequestExistsByIdAndClientEmail_whenCancelJourneyRequest_thenCancelJourneyRequest() {
		// given
		ClientJourneyRequestDtoOutput clientJourneyRequestDto = JourneyRequestTestData.defaultClientJourneyRequestDtoOutput();
		given(loadJourneyRequestPort.loadJourneyRequestByIdAndClientSubject(any(Long.class), any(String.class),
				any(String.class))).willReturn(Optional.of(clientJourneyRequestDto));

		Set<TransporterNotificationInfo> tniSet = Set.of(
				new TransporterNotificationInfo("transporter1DeviceRegistrationToken", "Africa/Tunis", "fr_FR"),
				new TransporterNotificationInfo("transporter2DeviceRegistrationToken", "Africa/Tunis", "en_US"),
				new TransporterNotificationInfo("transporter3DeviceRegistrationToken", "Africa/Tunis", "fr_FR"));
		given(loadProposalsPort.loadTransportersNotificationInfo(clientJourneyRequestDto.getId())).willReturn(tniSet);

		Map<String, String> departurePlaceNames = Map.of("fr_FR", "Sfax", "en_US", "Sfax");
		given(loadPlaceNamesPort.loadPlaceNames(clientJourneyRequestDto.getDeparturePlace().getId(),
				PlaceType.valueOf(clientJourneyRequestDto.getDeparturePlace().getType()), Set.of("fr_FR", "en_US")))
						.willReturn(departurePlaceNames);

		Map<String, String> arrivalPlaceNames = Map.of("fr_FR", "Tunis", "en_US", "Tunis");
		given(loadPlaceNamesPort.loadPlaceNames(clientJourneyRequestDto.getArrivalPlace().getId(),
				PlaceType.valueOf(clientJourneyRequestDto.getArrivalPlace().getType()), Set.of("fr_FR", "en_US")))
						.willReturn(arrivalPlaceNames);
		given(dateTimeHelper.systemToUserLocalDateTime(eq(clientJourneyRequestDto.getDateTime()), any(ZoneId.class)))
				.willReturn(LocalDateTime.of(2020, 12, 10, 12, 0));
		// when
		updateJourneyRequestStatusService.updateStatus(1L, TestConstants.DEFAULT_EMAIL,
				JourneyRequestStatusCode.CANCELED, "fr_FR");
		// then
		then(cancelJourneyRequestPort).should(times(1)).cancelJourneyRequest(1L);
		then(messagingPort).should(times(3)).sendPushMessage(any(PushMessage.class));
	}

	@Test
	void givenJourneyRequestExistsByIdAndClientEmail_andExceptionWhenSendingNotification_whenCancelJourneyRequest_thenCancelJourneyRequest() {
		// given
		ClientJourneyRequestDtoOutput clientJourneyRequestDto = JourneyRequestTestData.defaultClientJourneyRequestDtoOutput();
		given(loadJourneyRequestPort.loadJourneyRequestByIdAndClientSubject(any(Long.class), any(String.class),
				any(String.class))).willReturn(Optional.of(clientJourneyRequestDto));

		Set<TransporterNotificationInfo> tniSet = Set.of(
				new TransporterNotificationInfo("transporter1DeviceRegistrationToken", "Africa/Tunis", "fr_FR"),
				new TransporterNotificationInfo("transporter2DeviceRegistrationToken", "Africa/Tunis", "en_US"),
				new TransporterNotificationInfo("transporter3DeviceRegistrationToken", "Africa/Tunis", "fr_FR"));
		given(loadProposalsPort.loadTransportersNotificationInfo(clientJourneyRequestDto.getId())).willReturn(tniSet);

		Map<String, String> departurePlaceNames = Map.of("fr_FR", "Sfax", "en_US", "Sfax");
		given(loadPlaceNamesPort.loadPlaceNames(clientJourneyRequestDto.getDeparturePlace().getId(),
				PlaceType.valueOf(clientJourneyRequestDto.getDeparturePlace().getType()), Set.of("fr_FR", "en_US")))
						.willReturn(departurePlaceNames);

		Map<String, String> arrivalPlaceNames = Map.of("fr_FR", "Tunis", "en_US", "Tunis");
		given(loadPlaceNamesPort.loadPlaceNames(clientJourneyRequestDto.getArrivalPlace().getId(),
				PlaceType.valueOf(clientJourneyRequestDto.getArrivalPlace().getType()), Set.of("fr_FR", "en_US")))
						.willReturn(arrivalPlaceNames);
		given(dateTimeHelper.systemToUserLocalDateTime(eq(clientJourneyRequestDto.getDateTime()), any(ZoneId.class)))
				.willReturn(LocalDateTime.of(2020, 12, 10, 12, 0));

		doThrow(IllegalArgumentException.class).when(messagingPort).sendPushMessage(any(PushMessage.class));
		// when
		updateJourneyRequestStatusService.updateStatus(1L, TestConstants.DEFAULT_EMAIL,
				JourneyRequestStatusCode.CANCELED, "fr_FR");
		// then
		then(cancelJourneyRequestPort).should(times(1)).cancelJourneyRequest(1L);
		then(messagingPort).should(times(1)).sendPushMessage(any(PushMessage.class));
	}
}
