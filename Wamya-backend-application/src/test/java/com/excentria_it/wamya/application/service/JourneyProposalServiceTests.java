package com.excentria_it.wamya.application.service;

import static com.excentria_it.wamya.test.data.common.JourneyProposalTestData.*;
import static com.excentria_it.wamya.test.data.common.JourneyRequestTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
import com.excentria_it.wamya.application.port.out.LoadJourneyRequestPort;
import com.excentria_it.wamya.application.port.out.LoadProposalsPort;
import com.excentria_it.wamya.application.port.out.LoadTransporterVehiculesPort;
import com.excentria_it.wamya.application.port.out.MakeProposalPort;
import com.excentria_it.wamya.common.exception.InvalidTransporterVehiculeException;
import com.excentria_it.wamya.common.exception.JourneyRequestExpiredException;
import com.excentria_it.wamya.common.exception.JourneyRequestNotFoundException;
import com.excentria_it.wamya.domain.ClientJourneyRequestDto;
import com.excentria_it.wamya.domain.CreateJourneyRequestDto.CreateJourneyRequestDtoBuilder;
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

		createJourneyRequestDtoBuilder.dateTime(LocalDateTime.now(ZoneOffset.UTC).minusDays(1));
		createJourneyRequestDtoBuilder.endDateTime(LocalDateTime.now(ZoneOffset.UTC).plusDays(1));

		given(loadJourneyRequestPort.loadJourneyRequestById(any(Long.class)))
				.willReturn(Optional.of(createJourneyRequestDtoBuilder.build()));
		given(loadTransporterPort.loadTransporterVehicules(any(String.class)))
				.willReturn(Set.of(new VehiculeDto(VEHICULE_ID, CONSTRUCTOR_NAME, MODEL_NAME, PHOTO_URL)));
		given(makeProposalPort.makeProposal(any(String.class), any(Double.class), any(Long.class), any(Long.class)))
				.willReturn(1L);

		// when
		MakeProposalDto makeProposalDto = journeyProposalService.makeProposal(makeProposalCommand, 1L,
				TestConstants.DEFAULT_EMAIL);

		// then
		assertEquals(JOURNEY_PRICE, makeProposalDto.getPrice());
		assertEquals(JOURNEY_PRICE, makeProposalDto.getPrice());
		assertEquals(1L, makeProposalDto.getId());

	}

	@Test
	void givenMakeProposalCommandAndInexistentJourneyRequest_WhenMakeProposal_ThenThrowJourneyRequestNotFoundException() {
		// given
		MakeProposalCommand makeProposalCommand = new MakeProposalCommand(JOURNEY_PRICE, VEHICULE_ID);

		given(loadJourneyRequestPort.loadJourneyRequestById(any(Long.class))).willReturn(Optional.empty());

		// when // then
		assertThrows(JourneyRequestNotFoundException.class,
				() -> journeyProposalService.makeProposal(makeProposalCommand, 1L, TestConstants.DEFAULT_EMAIL));

	}

	@Test
	void givenMakeProposalCommandAndNullJourneyRequest_WhenMakeProposal_ThenThrowJourneyRequestNotFoundException() {
		// given
		MakeProposalCommand makeProposalCommand = new MakeProposalCommand(JOURNEY_PRICE, VEHICULE_ID);

		given(loadJourneyRequestPort.loadJourneyRequestById(any(Long.class))).willReturn(null);

		// when // then
		assertThrows(JourneyRequestNotFoundException.class,
				() -> journeyProposalService.makeProposal(makeProposalCommand, 1L, TestConstants.DEFAULT_EMAIL));

	}

	@Test
	void givenMakeProposalCommandAndExpiredJourneyRequest_WhenMakeProposal_ThenThrowJourneyRequestExpiredException() {
		// given
		MakeProposalCommand makeProposalCommand = new MakeProposalCommand(JOURNEY_PRICE, VEHICULE_ID);
		CreateJourneyRequestDtoBuilder createJourneyRequestDtoBuilder = defaultCreateJourneyRequestDtoBuilder();

		createJourneyRequestDtoBuilder.dateTime(LocalDateTime.now(ZoneOffset.UTC).minusDays(2));
		createJourneyRequestDtoBuilder.endDateTime(LocalDateTime.now(ZoneOffset.UTC).minusDays(1));

		given(loadJourneyRequestPort.loadJourneyRequestById(any(Long.class)))
				.willReturn(Optional.of(createJourneyRequestDtoBuilder.build()));

		// when // then
		assertThrows(JourneyRequestExpiredException.class,
				() -> journeyProposalService.makeProposal(makeProposalCommand, 1L, TestConstants.DEFAULT_EMAIL));

	}

	@Test
	void givenMakeProposalCommandAndEmptyTransporterVehicules_WhenMakeProposal_ThenThrowInvalidTransporterVehiculeException() {
		// given
		MakeProposalCommand makeProposalCommand = new MakeProposalCommand(JOURNEY_PRICE, VEHICULE_ID);

		CreateJourneyRequestDtoBuilder createJourneyRequestDtoBuilder = defaultCreateJourneyRequestDtoBuilder();

		createJourneyRequestDtoBuilder.dateTime(LocalDateTime.now(ZoneOffset.UTC).minusDays(1));
		createJourneyRequestDtoBuilder.endDateTime(LocalDateTime.now(ZoneOffset.UTC).plusDays(1));

		given(loadJourneyRequestPort.loadJourneyRequestById(any(Long.class)))
				.willReturn(Optional.of(createJourneyRequestDtoBuilder.build()));
		given(loadTransporterPort.loadTransporterVehicules(any(String.class)))
				.willReturn(Collections.<VehiculeDto>emptySet());

		// when //then
		assertThrows(InvalidTransporterVehiculeException.class,
				() -> journeyProposalService.makeProposal(makeProposalCommand, 1L, TestConstants.DEFAULT_EMAIL));

	}

	@Test
	void givenMakeProposalCommandAndUserEmailAndNotTransporterVehicule_WhenMakeProposal_ThenThrowInvalidTransporterVehiculeException() {
		// given
		MakeProposalCommand makeProposalCommand = new MakeProposalCommand(JOURNEY_PRICE, VEHICULE_ID);

		CreateJourneyRequestDtoBuilder createJourneyRequestDtoBuilder = defaultCreateJourneyRequestDtoBuilder();

		createJourneyRequestDtoBuilder.dateTime(LocalDateTime.now(ZoneOffset.UTC).minusDays(1));
		createJourneyRequestDtoBuilder.endDateTime(LocalDateTime.now(ZoneOffset.UTC).plusDays(1));

		given(loadJourneyRequestPort.loadJourneyRequestById(any(Long.class)))
				.willReturn(Optional.of(createJourneyRequestDtoBuilder.build()));
		given(loadTransporterPort.loadTransporterVehicules(any(String.class)))
				.willReturn(Set.of(new VehiculeDto(VEHICULE_ID + 2, CONSTRUCTOR_NAME, MODEL_NAME, PHOTO_URL)));

		// when //then
		assertThrows(InvalidTransporterVehiculeException.class,
				() -> journeyProposalService.makeProposal(makeProposalCommand, 1L, TestConstants.DEFAULT_EMAIL));

	}

	@Test
	void givenMakeProposalCommandAndUserMobileNumberAndNotTransporterVehicule_WhenMakeProposal_ThenThrowInvalidTransporterVehiculeException() {
		// given
		MakeProposalCommand makeProposalCommand = defaultMakeProposalCommandBuilder().build();

		CreateJourneyRequestDtoBuilder createJourneyRequestDtoBuilder = defaultCreateJourneyRequestDtoBuilder();

		createJourneyRequestDtoBuilder.dateTime(LocalDateTime.now(ZoneOffset.UTC).minusDays(1));
		createJourneyRequestDtoBuilder.endDateTime(LocalDateTime.now(ZoneOffset.UTC).plusDays(1));

		given(loadJourneyRequestPort.loadJourneyRequestById(any(Long.class)))
				.willReturn(Optional.of(createJourneyRequestDtoBuilder.build()));
		given(loadTransporterPort.loadTransporterVehicules(any(String.class), any(String.class)))
				.willReturn(Set.of(new VehiculeDto(VEHICULE_ID + 2, CONSTRUCTOR_NAME, MODEL_NAME, PHOTO_URL)));

		// when //then
		assertThrows(InvalidTransporterVehiculeException.class, () -> journeyProposalService
				.makeProposal(makeProposalCommand, 1L, TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME));

	}

	@Test
	void givenLoadProposalsCommandAndClientEmail_WhenLoadProposals_ThenSucceed() {
		// given
		LoadProposalsCommand command = defaultLoadProposalsCommandBuilder().build();
		ClientJourneyRequestDto clientJourneyRequestDto = defaultClientJourneyRequestDto();
		given(loadJourneyRequestPort.loadJourneyRequestByIdAndClientEmail(any(Long.class), any(String.class)))
				.willReturn(Optional.of(clientJourneyRequestDto));

		JourneyRequestProposals journeyRequestProposals = defaultJourneyRequestProposals();
		given(loadPropsalsPort.loadJourneyProposals(any(LoadJourneyProposalsCriteria.class)))
				.willReturn(journeyRequestProposals);
		// when

		JourneyRequestProposals result = journeyProposalService.loadProposals(command);
		// then

		then(loadPropsalsPort).should(times(1)).loadJourneyProposals(any(LoadJourneyProposalsCriteria.class));

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
		given(loadPropsalsPort.loadJourneyProposals(any(LoadJourneyProposalsCriteria.class)))
				.willReturn(journeyRequestProposals);
		// when

		JourneyRequestProposals result = journeyProposalService.loadProposals(command);
		// then

		then(loadPropsalsPort).should(times(1)).loadJourneyProposals(any(LoadJourneyProposalsCriteria.class));

		assertThat(result).isEqualTo(journeyRequestProposals);

	}

	@Test
	void givenLoadProposalsCommandAndInexistentClientJourneyRequest_WhenLoadProposals_ThenThrowJourneyRequestNotFoundException() {
		// given
		LoadProposalsCommand command = defaultLoadProposalsCommandBuilder().build();

		given(loadJourneyRequestPort.loadJourneyRequestByIdAndClientEmail(any(Long.class), any(String.class)))
				.willReturn(Optional.empty());

		// when // then

		assertThrows(JourneyRequestNotFoundException.class, () -> journeyProposalService.loadProposals(command));

	}

	@Test
	void givenLoadProposalsCommandAndNullClientJourneyRequest_WhenLoadProposals_ThenThrowJourneyRequestNotFoundException() {
		// given
		LoadProposalsCommand command = defaultLoadProposalsCommandBuilder().build();

		given(loadJourneyRequestPort.loadJourneyRequestByIdAndClientEmail(any(Long.class), any(String.class)))
				.willReturn(null);

		// when // then

		assertThrows(JourneyRequestNotFoundException.class, () -> journeyProposalService.loadProposals(command));

	}
}
