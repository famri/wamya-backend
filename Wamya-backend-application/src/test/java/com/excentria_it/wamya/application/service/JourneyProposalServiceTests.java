package com.excentria_it.wamya.application.service;

import static com.excentria_it.wamya.test.data.common.JourneyProposalTestData.*;
import static com.excentria_it.wamya.test.data.common.JourneyRequestTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.in.LoadProposalsUseCase.LoadProposalsCommand;
import com.excentria_it.wamya.application.port.in.MakeProposalUseCase.MakeProposalCommand;
import com.excentria_it.wamya.application.port.out.AcceptProposalPort;
import com.excentria_it.wamya.application.port.out.CheckUserVehiculePort;
import com.excentria_it.wamya.application.port.out.LoadJourneyRequestPort;
import com.excentria_it.wamya.application.port.out.LoadProposalsPort;
import com.excentria_it.wamya.application.port.out.LoadTransporterVehiculesPort;
import com.excentria_it.wamya.application.port.out.MakeProposalPort;
import com.excentria_it.wamya.application.port.out.RejectProposalPort;
import com.excentria_it.wamya.common.domain.StatusCode;
import com.excentria_it.wamya.common.exception.InvalidTransporterVehiculeException;
import com.excentria_it.wamya.common.exception.JourneyProposalNotFoundException;
import com.excentria_it.wamya.common.exception.JourneyRequestExpiredException;
import com.excentria_it.wamya.common.exception.JourneyRequestNotFoundException;
import com.excentria_it.wamya.domain.ClientJourneyRequestDtoOutput;
import com.excentria_it.wamya.domain.JourneyRequestInputOutput.JourneyRequestInputOutputBuilder;
import com.excentria_it.wamya.domain.JourneyRequestProposals;
import com.excentria_it.wamya.domain.LoadJourneyProposalsCriteria;
import com.excentria_it.wamya.domain.MakeProposalDto;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ExtendWith(MockitoExtension.class)
public class JourneyProposalServiceTests {
	@Mock
	private MakeProposalPort makeProposalPort;
	@Mock
	private LoadTransporterVehiculesPort loadTransporterPort;
	@Mock
	private LoadJourneyRequestPort loadJourneyRequestPort;
	@Mock
	private LoadProposalsPort loadPropsalsPort;
	@Mock
	private AcceptProposalPort acceptProposalPort;
	@Mock
	private RejectProposalPort rejectProposalPort;
	@Mock
	private CheckUserVehiculePort checkUserVehiculePort;

	@InjectMocks
	private JourneyProposalService journeyProposalService;

	private static final Long VEHICULE_ID = 1L;

	private static final Double JOURNEY_PRICE = 250.0;

	private static final String CONSTRUCTOR_NAME = "PEUGEOT";

	private static final String MODEL_NAME = "PARTNER";

	private static final String PHOTO_URL = "http://some/vehicule/photo/url";

	@Test
	void givenMakeProposalCommand_WhenMakeProposal_ThenSucceed() {
		// given
		MakeProposalCommand makeProposalCommand = new MakeProposalCommand(JOURNEY_PRICE, VEHICULE_ID);

		JourneyRequestInputOutputBuilder journeyRequestInputOutputBuilder = defaultJourneyRequestInputOutputBuilder();

		journeyRequestInputOutputBuilder.dateTime(ZonedDateTime.now(ZoneOffset.UTC).plusDays(1).toInstant());

		given(loadJourneyRequestPort.loadJourneyRequestById(any(Long.class)))
				.willReturn(Optional.of(journeyRequestInputOutputBuilder.build()));
		given(checkUserVehiculePort.isUserVehicule(any(String.class), any(Long.class))).willReturn(true);
		given(makeProposalPort.makeProposal(any(String.class), any(Double.class), any(Long.class), any(Long.class),
				any(String.class))).willReturn(defaultMakeProposalDto());

		// when
		MakeProposalDto makeProposalDto = journeyProposalService.makeProposal(makeProposalCommand, 1L,
				TestConstants.DEFAULT_EMAIL, "en_US");

		// then
		assertEquals(JOURNEY_PRICE, makeProposalDto.getPrice());
		assertEquals(1L, makeProposalDto.getId());

	}

	@Test
	void givenMakeProposalCommandAndInexistentJourneyRequest_WhenMakeProposal_ThenThrowJourneyRequestNotFoundException() {
		// given
		MakeProposalCommand makeProposalCommand = new MakeProposalCommand(JOURNEY_PRICE, VEHICULE_ID);

		given(loadJourneyRequestPort.loadJourneyRequestById(any(Long.class))).willReturn(Optional.empty());

		// when // then
		assertThrows(JourneyRequestNotFoundException.class, () -> journeyProposalService
				.makeProposal(makeProposalCommand, 1L, TestConstants.DEFAULT_EMAIL, "en_US"));

	}

	@Test
	void givenMakeProposalCommandAndExpiredJourneyRequest_WhenMakeProposal_ThenThrowJourneyRequestExpiredException() {
		// given
		MakeProposalCommand makeProposalCommand = new MakeProposalCommand(JOURNEY_PRICE, VEHICULE_ID);
		JourneyRequestInputOutputBuilder journeyRequestInputOutputBuilder = defaultJourneyRequestInputOutputBuilder();

		journeyRequestInputOutputBuilder.dateTime(ZonedDateTime.now(ZoneOffset.UTC).minusDays(2).toInstant());

		given(loadJourneyRequestPort.loadJourneyRequestById(any(Long.class)))
				.willReturn(Optional.of(journeyRequestInputOutputBuilder.build()));

		// when // then
		assertThrows(JourneyRequestExpiredException.class, () -> journeyProposalService
				.makeProposal(makeProposalCommand, 1L, TestConstants.DEFAULT_EMAIL, "en_US"));

	}

	@Test
	void givenMakeProposalCommandAndUserEmailAndNotTransporterVehicule_WhenMakeProposal_ThenThrowInvalidTransporterVehiculeException() {
		// given
		MakeProposalCommand makeProposalCommand = new MakeProposalCommand(JOURNEY_PRICE, VEHICULE_ID);

		JourneyRequestInputOutputBuilder journeyRequestInputOutputBuilder = defaultJourneyRequestInputOutputBuilder();

		journeyRequestInputOutputBuilder.dateTime(ZonedDateTime.now(ZoneOffset.UTC).plusDays(1).toInstant());

		given(loadJourneyRequestPort.loadJourneyRequestById(any(Long.class)))
				.willReturn(Optional.of(journeyRequestInputOutputBuilder.build()));
		given(checkUserVehiculePort.isUserVehicule(any(String.class), any(Long.class))).willReturn(false);

		// when //then
		assertThrows(InvalidTransporterVehiculeException.class, () -> journeyProposalService
				.makeProposal(makeProposalCommand, 1L, TestConstants.DEFAULT_EMAIL, "en_US"));

	}

	@Test
	void givenLoadProposalsCommandAndClientEmail_WhenLoadProposals_ThenSucceed() {
		// given
		LoadProposalsCommand command = defaultLoadProposalsCommandBuilder().build();
		ClientJourneyRequestDtoOutput clientJourneyRequestDto = defaultClientJourneyRequestDtoOutput();
		given(loadJourneyRequestPort.loadJourneyRequestByIdAndClientEmail(any(Long.class), any(String.class),
				any(String.class))).willReturn(Optional.of(clientJourneyRequestDto));

		JourneyRequestProposals journeyRequestProposals = defaultJourneyRequestProposals();
		given(loadPropsalsPort.loadJourneyProposals(any(LoadJourneyProposalsCriteria.class), any(String.class)))
				.willReturn(journeyRequestProposals);
		// when

		JourneyRequestProposals result = journeyProposalService.loadProposals(command, "en_US");
		// then

		then(loadPropsalsPort).should(times(1)).loadJourneyProposals(any(LoadJourneyProposalsCriteria.class),
				eq("en_US"));

		assertThat(result).isEqualTo(journeyRequestProposals);

	}

	@Test
	void givenLoadProposalsCommandAndInexistentClientJourneyRequest_WhenLoadProposals_ThenThrowJourneyRequestNotFoundException() {
		// given
		LoadProposalsCommand command = defaultLoadProposalsCommandBuilder().build();

		given(loadJourneyRequestPort.loadJourneyRequestByIdAndClientEmail(any(Long.class), any(String.class),
				any(String.class))).willReturn(Optional.empty());

		// when // then

		assertThrows(JourneyRequestNotFoundException.class,
				() -> journeyProposalService.loadProposals(command, "en_US"));

	}

	@Test
	void givenClientEmailAndInexistentClientJourneyRequest_WhenAcceptProposal_ThenThrowJourneyRequestNotFoundException() {

		// given
		given(loadJourneyRequestPort.isExistentAndNotExpiredJourneyRequestByIdAndClientEmail(any(Long.class),
				any(String.class))).willReturn(false);

		// when //then
		assertThrows(JourneyRequestNotFoundException.class,
				() -> journeyProposalService.updateProposal(1L, 1L, StatusCode.ACCEPTED, TestConstants.DEFAULT_EMAIL));

	}



	@Test
	void givenClientEmailAndInexistentJourneyProposal_WhenAcceptProposal_ThenThrowJourneyProposalNotFoundException() {

		// given
		given(loadJourneyRequestPort.isExistentAndNotExpiredJourneyRequestByIdAndClientEmail(any(Long.class),
				any(String.class))).willReturn(true);

		given(loadPropsalsPort.isExistentJourneyProposalByIdAndJourneyRequestIdAndStatusCode(any(Long.class),
				any(Long.class), any(StatusCode.class))).willReturn(false);

		// when //then
		assertThrows(JourneyProposalNotFoundException.class,
				() -> journeyProposalService.updateProposal(1L, 1L, StatusCode.ACCEPTED, TestConstants.DEFAULT_EMAIL));

	}

	@Test
	void givenValidParameters_WhenAcceptProposal_ThenSucceed() {

		// given

		given(loadJourneyRequestPort.isExistentAndNotExpiredJourneyRequestByIdAndClientEmail(any(Long.class),
				any(String.class))).willReturn(true);

		given(loadPropsalsPort.isExistentJourneyProposalByIdAndJourneyRequestIdAndStatusCode(any(Long.class),
				any(Long.class), any(StatusCode.class))).willReturn(true);

		// when
		journeyProposalService.updateProposal(1L, 1L, StatusCode.ACCEPTED, TestConstants.DEFAULT_EMAIL);
		// then
		then(acceptProposalPort).should(times(1)).acceptProposal(eq(1L), eq(1L));
	}

	@Test
	void givenClientEmailAndInexistentClientJourneyRequest_WhenRejectProposal_ThenThrowJourneyRequestNotFoundException() {

		// given
		given(loadJourneyRequestPort.isExistentAndNotExpiredJourneyRequestByIdAndClientEmail(any(Long.class),
				any(String.class))).willReturn(false);

		// when //then
		assertThrows(JourneyRequestNotFoundException.class,
				() -> journeyProposalService.updateProposal(1L, 1L, StatusCode.REJECTED, TestConstants.DEFAULT_EMAIL));

	}

	@Test
	void givenClientEmailAndInexistentJourneyProposal_WhenRejectProposal_ThenThrowJourneyProposalNotFoundException() {

		// given
		given(loadJourneyRequestPort.isExistentAndNotExpiredJourneyRequestByIdAndClientEmail(any(Long.class),
				any(String.class))).willReturn(true);

		given(loadPropsalsPort.isExistentJourneyProposalByIdAndJourneyRequestIdAndStatusCode(any(Long.class),
				any(Long.class), any(StatusCode.class))).willReturn(false);

		// when //then
		assertThrows(JourneyProposalNotFoundException.class,
				() -> journeyProposalService.updateProposal(1L, 1L, StatusCode.REJECTED, TestConstants.DEFAULT_EMAIL));

	}

	@Test
	void givenValidParameters_WhenRejectProposal_ThenSucceed() {

		// given

		given(loadJourneyRequestPort.isExistentAndNotExpiredJourneyRequestByIdAndClientEmail(any(Long.class),
				any(String.class))).willReturn(true);

		given(loadPropsalsPort.isExistentJourneyProposalByIdAndJourneyRequestIdAndStatusCode(any(Long.class),
				any(Long.class), any(StatusCode.class))).willReturn(true);

		// when
		journeyProposalService.updateProposal(1L, 1L, StatusCode.REJECTED, TestConstants.DEFAULT_EMAIL);
		// then
		then(rejectProposalPort).should(times(1)).rejectProposal(eq(1L), eq(1L));
	}

}
