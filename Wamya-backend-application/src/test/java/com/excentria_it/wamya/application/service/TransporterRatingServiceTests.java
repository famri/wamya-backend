package com.excentria_it.wamya.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.ZoneId;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.in.LoadTransporterRatingRequestUseCase.LoadTransporterRatingRequestCommand;
import com.excentria_it.wamya.application.port.out.LoadTransporterRatingRequestRecordPort;
import com.excentria_it.wamya.application.utils.DateTimeHelper;
import com.excentria_it.wamya.common.exception.TransporterRatingDetailsNotFoundException;
import com.excentria_it.wamya.domain.TransporterRatingRequestRecordDto;
import com.excentria_it.wamya.domain.TransporterRatingRequestRecordOutput;
import com.excentria_it.wamya.test.data.common.TransporterRatingRequestTestData;

@ExtendWith(MockitoExtension.class)
public class TransporterRatingServiceTests {
	@Mock
	private LoadTransporterRatingRequestRecordPort loadTransporterRatingDetailsPort;

	@Spy
	private DateTimeHelper dateTimeHelper;

	@InjectMocks
	private TransporterRatingService transporterRatingService;

	@Test
	void givenEmptyTransporterRatingDetailsDto_whenLoadTransporterRatingDetails_ThenThrowTransporterRatingDetailsNotFoundException() {

		// given
		given(loadTransporterRatingDetailsPort.loadRecord(any(String.class), any(Long.class),
				any(String.class))).willReturn(Optional.empty());

		LoadTransporterRatingRequestCommand command = LoadTransporterRatingRequestCommand.builder().userId(1L)
				.hash("SOME_HASH").build();
		// when

		// then

		assertThrows(TransporterRatingDetailsNotFoundException.class,
				() -> transporterRatingService.loadTransporterRatingRequest(command, "fr_FR"));
	}

	@Test
	void givenExistentTransporterRatingDetailsDto_whenLoadTransporterRatingDetails_ThenReturnTransporterRatingDetailsDto() {

		// given
		TransporterRatingRequestRecordOutput trdo = TransporterRatingRequestTestData.defaultTransporterRatingRequestRecordOutput();

		given(loadTransporterRatingDetailsPort.loadRecord(any(String.class), any(Long.class),
				any(String.class))).willReturn(Optional.of(trdo));

		LoadTransporterRatingRequestCommand command = LoadTransporterRatingRequestCommand.builder().userId(1L)
				.hash("SOME_HASH").build();

		doReturn(ZoneId.of("Africa/Tunis")).when(dateTimeHelper).findUserZoneId(command.getUserId());

		// when
		TransporterRatingRequestRecordDto result = transporterRatingService.loadTransporterRatingRequest(command, "fr_FR");
		// then

		assertEquals(trdo.getJourneyRequest().getDeparturePlace().getName(),
				result.getJourneyRequest().getDeparturePlace().getName());
		assertEquals(trdo.getJourneyRequest().getArrivalPlace().getName(),
				result.getJourneyRequest().getArrivalPlace().getName());
		assertEquals(dateTimeHelper.systemToUserLocalDateTime(trdo.getJourneyRequest().getDateTime(),
				ZoneId.of("Africa/Tunis")), result.getJourneyRequest().getDateTime());

		assertEquals(trdo.getTransporter().getFirstname(), result.getTransporter().getFirstname());
		assertEquals(trdo.getTransporter().getPhotoUrl(), result.getTransporter().getPhotoUrl());
		assertEquals(trdo.getTransporter().getGlobalRating(), result.getTransporter().getGlobalRating());

	}

}
