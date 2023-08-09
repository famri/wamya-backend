package com.excentria_it.wamya.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.in.SendTransporterRatingUseCase.SendTransporterRatingCommand;
import com.excentria_it.wamya.application.port.out.LoadTransporterRatingRequestRecordPort;
import com.excentria_it.wamya.application.port.out.SaveTransporterRatingPort;
import com.excentria_it.wamya.application.port.out.UpdateTransporterRatingRequestRecordPort;
import com.excentria_it.wamya.common.exception.TransporterRatingRequestNotFoundException;
import com.excentria_it.wamya.domain.TransporterRatingRequestRecordOutput;
import com.excentria_it.wamya.domain.TransporterRatingRequestStatus;
import com.excentria_it.wamya.test.data.common.TransporterRatingRequestTestData;

@ExtendWith(MockitoExtension.class)
public class SendTransporterRatingServiceTests {
	@Mock
	private LoadTransporterRatingRequestRecordPort loadTransporterRatingRequestPort;
	@Mock
	private SaveTransporterRatingPort saveTransporterRatingPort;
	@Mock
	private UpdateTransporterRatingRequestRecordPort updateTransporterRatingRequestPort;

	@InjectMocks
	private SendTransporterRatingService sendTransporterRatingService;

	@Test
	void testSaveTransporterRating() {
		// given
		TransporterRatingRequestRecordOutput tro = TransporterRatingRequestTestData
				.defaultTransporterRatingRequestRecordOutput();

		given(loadTransporterRatingRequestPort.loadRecord(any(String.class), any(Long.class), any(String.class)))
				.willReturn(Optional.of(tro));
		SendTransporterRatingCommand command = SendTransporterRatingCommand.builder().uid(1L).hash("SOME_HASH")
				.rating(5).comment("SOME COMMENT").build();
		// when
		sendTransporterRatingService.saveTransporterRating(command, "fr_FR");

		// then
		then(saveTransporterRatingPort).should((times(1))).saveRating(command.getUid(), command.getRating(),
				command.getComment(), tro.getTransporter().getId());

		then(updateTransporterRatingRequestPort).should(times(1)).updateRequestStatus(tro.getId(),
				TransporterRatingRequestStatus.FULFILLED);
	}

	@Test
	void givenTransporterRatingRequestRecordOutputNotFound_whenSaveTransporterRating_thenThrowTransporterRatingRequestNotFoundException() {
		// given

		given(loadTransporterRatingRequestPort.loadRecord(any(String.class), any(Long.class), any(String.class)))
				.willReturn(Optional.empty());
		SendTransporterRatingCommand command = SendTransporterRatingCommand.builder().uid(1L).hash("SOME_HASH")
				.rating(5).comment("SOME COMMENT").build();
		// when
		// then
		assertThrows(TransporterRatingRequestNotFoundException.class,
				() -> sendTransporterRatingService.saveTransporterRating(command, "fr_FR"));

	}
	
	
}
