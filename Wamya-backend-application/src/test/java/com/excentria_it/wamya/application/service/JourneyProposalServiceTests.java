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
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.in.LoadProposalsUseCase.LoadProposalsCommand;
import com.excentria_it.wamya.application.port.in.MakeProposalUseCase.MakeProposalCommand;
import com.excentria_it.wamya.application.port.out.AcceptProposalPort;
import com.excentria_it.wamya.application.port.out.LoadJourneyRequestPort;
import com.excentria_it.wamya.application.port.out.LoadProposalsPort;
import com.excentria_it.wamya.application.port.out.LoadTransporterVehiculesPort;
import com.excentria_it.wamya.application.port.out.MakeProposalPort;
import com.excentria_it.wamya.common.exception.InvalidTransporterVehiculeException;
import com.excentria_it.wamya.common.exception.JourneyProposalNotFoundException;
import com.excentria_it.wamya.common.exception.JourneyRequestExpiredException;
import com.excentria_it.wamya.common.exception.JourneyRequestNotFoundException;
import com.excentria_it.wamya.common.exception.OperationDeniedException;
import com.excentria_it.wamya.domain.ClientJourneyRequestDto;
import com.excentria_it.wamya.domain.CreateJourneyRequestDto.CreateJourneyRequestDtoBuilder;
import com.excentria_it.wamya.domain.JourneyProposalDto;
import com.excentria_it.wamya.domain.JourneyProposalDto.StatusCode;
import com.excentria_it.wamya.domain.JourneyProposalDto.StatusDto;
import com.excentria_it.wamya.domain.JourneyProposalDto.VehiculeDto;
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

		CreateJourneyRequestDtoBuilder createJourneyRequestDtoBuilder = defaultCreateJourneyRequestDtoBuilder();

		createJourneyRequestDtoBuilder.dateTime(ZonedDateTime.now(ZoneOffset.UTC).plusDays(1).toInstant());

		given(loadJourneyRequestPort.loadJourneyRequestById(any(Long.class)))
				.willReturn(Optional.of(createJourneyRequestDtoBuilder.build()));
		given(loadTransporterPort.loadTransporterVehicules(any(String.class)))
				.willReturn(Set.of(new VehiculeDto(VEHICULE_ID, CONSTRUCTOR_NAME, MODEL_NAME, PHOTO_URL)));
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
		CreateJourneyRequestDtoBuilder createJourneyRequestDtoBuilder = defaultCreateJourneyRequestDtoBuilder();

		createJourneyRequestDtoBuilder.dateTime(ZonedDateTime.now(ZoneOffset.UTC).minusDays(2).toInstant());

		given(loadJourneyRequestPort.loadJourneyRequestById(any(Long.class)))
				.willReturn(Optional.of(createJourneyRequestDtoBuilder.build()));

		// when // then
		assertThrows(JourneyRequestExpiredException.class, () -> journeyProposalService
				.makeProposal(makeProposalCommand, 1L, TestConstants.DEFAULT_EMAIL, "en_US"));

	}

	@Test
	void givenMakeProposalCommandAndEmptyTransporterVehicules_WhenMakeProposal_ThenThrowInvalidTransporterVehiculeException() {
		// given
		MakeProposalCommand makeProposalCommand = new MakeProposalCommand(JOURNEY_PRICE, VEHICULE_ID);

		CreateJourneyRequestDtoBuilder createJourneyRequestDtoBuilder = defaultCreateJourneyRequestDtoBuilder();

		createJourneyRequestDtoBuilder.dateTime(ZonedDateTime.now(ZoneOffset.UTC).plusDays(1).toInstant());

		given(loadJourneyRequestPort.loadJourneyRequestById(any(Long.class)))
				.willReturn(Optional.of(createJourneyRequestDtoBuilder.build()));
		given(loadTransporterPort.loadTransporterVehicules(any(String.class)))
				.willReturn(Collections.<VehiculeDto>emptySet());

		// when //then
		assertThrows(InvalidTransporterVehiculeException.class, () -> journeyProposalService
				.makeProposal(makeProposalCommand, 1L, TestConstants.DEFAULT_EMAIL, "en_US"));

	}

	@Test
	void givenMakeProposalCommandAndUserEmailAndNotTransporterVehicule_WhenMakeProposal_ThenThrowInvalidTransporterVehiculeException() {
		// given
		MakeProposalCommand makeProposalCommand = new MakeProposalCommand(JOURNEY_PRICE, VEHICULE_ID);

		CreateJourneyRequestDtoBuilder createJourneyRequestDtoBuilder = defaultCreateJourneyRequestDtoBuilder();

		createJourneyRequestDtoBuilder.dateTime(ZonedDateTime.now(ZoneOffset.UTC).plusDays(1).toInstant());

		given(loadJourneyRequestPort.loadJourneyRequestById(any(Long.class)))
				.willReturn(Optional.of(createJourneyRequestDtoBuilder.build()));
		given(loadTransporterPort.loadTransporterVehicules(any(String.class)))
				.willReturn(Set.of(new VehiculeDto(VEHICULE_ID + 2, CONSTRUCTOR_NAME, MODEL_NAME, PHOTO_URL)));

		// when //then
		assertThrows(InvalidTransporterVehiculeException.class, () -> journeyProposalService
				.makeProposal(makeProposalCommand, 1L, TestConstants.DEFAULT_EMAIL, "en_US"));

	}

	@Test
	void givenMakeProposalCommandAndUserMobileNumberAndNotTransporterVehicule_WhenMakeProposal_ThenThrowInvalidTransporterVehiculeException() {
		// given
		MakeProposalCommand makeProposalCommand = defaultMakeProposalCommandBuilder().build();

		CreateJourneyRequestDtoBuilder createJourneyRequestDtoBuilder = defaultCreateJourneyRequestDtoBuilder();

		createJourneyRequestDtoBuilder.dateTime(ZonedDateTime.now(ZoneOffset.UTC).plusDays(1).toInstant());

		given(loadJourneyRequestPort.loadJourneyRequestById(any(Long.class)))
				.willReturn(Optional.of(createJourneyRequestDtoBuilder.build()));
		given(loadTransporterPort.loadTransporterVehicules(any(String.class), any(String.class)))
				.willReturn(Set.of(new VehiculeDto(VEHICULE_ID + 2, CONSTRUCTOR_NAME, MODEL_NAME, PHOTO_URL)));

		// when //then
		assertThrows(InvalidTransporterVehiculeException.class, () -> journeyProposalService
				.makeProposal(makeProposalCommand, 1L, TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME, "en_US"));

	}

	@Test
	void givenLoadProposalsCommandAndClientEmail_WhenLoadProposals_ThenSucceed() {
		// given
		LoadProposalsCommand command = defaultLoadProposalsCommandBuilder().build();
		ClientJourneyRequestDto clientJourneyRequestDto = defaultClientJourneyRequestDto();
		given(loadJourneyRequestPort.loadJourneyRequestByIdAndClientEmail(any(Long.class), any(String.class)))
				.willReturn(Optional.of(clientJourneyRequestDto));

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
	void givenLoadProposalsCommandAndClientMobileNumber_WhenLoadProposals_ThenSucceed() {
		// given
		LoadProposalsCommand command = defaultLoadProposalsCommandBuilder()
				.clientUsername(TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME).build();
		ClientJourneyRequestDto clientJourneyRequestDto = defaultClientJourneyRequestDto();
		given(loadJourneyRequestPort.loadJourneyRequestByIdAndClientMobileNumberAndIcc(any(Long.class),
				any(String.class), any(String.class))).willReturn(Optional.of(clientJourneyRequestDto));

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

		given(loadJourneyRequestPort.loadJourneyRequestByIdAndClientEmail(any(Long.class), any(String.class)))
				.willReturn(Optional.empty());

		// when // then

		assertThrows(JourneyRequestNotFoundException.class,
				() -> journeyProposalService.loadProposals(command, "en_US"));

	}

	@Test
	void givenClientEmailAndEmptyClientJourneyRequest_WhenAcceptProposal_ThenThrowJourneyRequestNotFoundException() {

		// given
		given(loadJourneyRequestPort.loadJourneyRequestByIdAndClientEmail(any(Long.class), any(String.class)))
				.willReturn(Optional.empty());

		// when //then
		assertThrows(JourneyRequestNotFoundException.class,
				() -> journeyProposalService.acceptProposal(1L, 1L, TestConstants.DEFAULT_EMAIL, "en_US"));

	}

	@Test
	void givenClientMobileNumberAndEmptyClientJourneyRequest_WhenAcceptProposal_ThenThrowJourneyRequestNotFoundException() {

		// given
		given(loadJourneyRequestPort.loadJourneyRequestByIdAndClientMobileNumberAndIcc(any(Long.class),
				any(String.class), any(String.class))).willReturn(Optional.empty());

		// when //then
		assertThrows(JourneyRequestNotFoundException.class, () -> journeyProposalService.acceptProposal(1L, 1L,
				TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME, "en_US"));

	}

	@Test
	void givenClientEmailAndEmptyClientJourneyProposal_WhenAcceptProposal_ThenThrowJourneyProposalNotFoundException() {

		// given
		ClientJourneyRequestDto journeyRequest = defaultClientJourneyRequestDto();
		given(loadJourneyRequestPort.loadJourneyRequestByIdAndClientEmail(any(Long.class), any(String.class)))
				.willReturn(Optional.of(journeyRequest));

		given(loadPropsalsPort.loadJourneyProposalByIdAndJourneyRequestId(any(Long.class), any(Long.class),
				any(String.class))).willReturn(Optional.empty());

		// when //then
		assertThrows(JourneyProposalNotFoundException.class,
				() -> journeyProposalService.acceptProposal(1L, 1L, TestConstants.DEFAULT_EMAIL, "en_US"));

	}

	@Test
	void givenNullJourneyProposalStatus_WhenAcceptProposal_ThenThrowOperationDeniedException() {

		// given
		ClientJourneyRequestDto journeyRequest = defaultClientJourneyRequestDto();
		given(loadJourneyRequestPort.loadJourneyRequestByIdAndClientEmail(any(Long.class), any(String.class)))
				.willReturn(Optional.of(journeyRequest));

		JourneyProposalDto journeyProposalDto = defaultJourneyProposalDto();
		journeyProposalDto.setStatus(null);

		given(loadPropsalsPort.loadJourneyProposalByIdAndJourneyRequestId(any(Long.class), any(Long.class),
				any(String.class))).willReturn(Optional.of(journeyProposalDto));

		// when //then
		assertThrows(OperationDeniedException.class,
				() -> journeyProposalService.acceptProposal(1L, 1L, TestConstants.DEFAULT_EMAIL, "en_US"));

	}

	@Test
	void givenJourneyProposalStatusIsNotSubmitted_WhenAcceptProposal_ThenThrowOperationDeniedException() {

		// given
		ClientJourneyRequestDto journeyRequest = defaultClientJourneyRequestDto();
		given(loadJourneyRequestPort.loadJourneyRequestByIdAndClientEmail(any(Long.class), any(String.class)))
				.willReturn(Optional.of(journeyRequest));

		JourneyProposalDto journeyProposalDto = defaultJourneyProposalDto();
		journeyProposalDto.setStatus(new StatusDto(StatusCode.CANCELED, "cancled"));

		given(loadPropsalsPort.loadJourneyProposalByIdAndJourneyRequestId(any(Long.class), any(Long.class),
				any(String.class))).willReturn(Optional.of(journeyProposalDto));

		// when //then
		assertThrows(OperationDeniedException.class,
				() -> journeyProposalService.acceptProposal(1L, 1L, TestConstants.DEFAULT_EMAIL, "en_US"));

	}

	@Test
	void givenValidParameters_WhenAcceptProposal_ThenSucceed() {

		// given
		ClientJourneyRequestDto journeyRequest = defaultClientJourneyRequestDto();
		given(loadJourneyRequestPort.loadJourneyRequestByIdAndClientEmail(any(Long.class), any(String.class)))
				.willReturn(Optional.of(journeyRequest));

		JourneyProposalDto journeyProposalDto = defaultJourneyProposalDto();
		journeyProposalDto.setStatus(new StatusDto(StatusCode.SUBMITTED, "submitted"));
		given(loadPropsalsPort.loadJourneyProposalByIdAndJourneyRequestId(any(Long.class), any(Long.class),
				any(String.class))).willReturn(Optional.of(journeyProposalDto));

		// when
		journeyProposalService.acceptProposal(1L, 1L, TestConstants.DEFAULT_EMAIL, "en_US");
		// then
		then(acceptProposalPort).should(times(1)).acceptProposal(eq(1L), eq(1L));
	}

}
