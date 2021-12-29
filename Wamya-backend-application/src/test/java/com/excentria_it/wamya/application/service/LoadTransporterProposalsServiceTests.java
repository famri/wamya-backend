package com.excentria_it.wamya.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.in.LoadTransporterProposalsUseCase.LoadTransporterProposalsCommand;
import com.excentria_it.wamya.application.port.out.LoadTransporterProposalsPort;
import com.excentria_it.wamya.application.utils.DateTimeHelper;
import com.excentria_it.wamya.application.utils.DocumentUrlResolver;
import com.excentria_it.wamya.domain.LoadTransporterProposalsCriteria;
import com.excentria_it.wamya.domain.TransporterProposals;
import com.excentria_it.wamya.domain.TransporterProposalsOutput;
import com.excentria_it.wamya.test.data.common.JourneyProposalTestData;

@ExtendWith(MockitoExtension.class)
public class LoadTransporterProposalsServiceTests {
	@Mock
	private LoadTransporterProposalsPort loadTransporterPropsalsPort;
	@Mock
	private DateTimeHelper dateTimeHelper;
	@Mock
	private DocumentUrlResolver documentUrlResolver;

	@InjectMocks
	@Spy
	private LoadTransporterProposalsService loadTransporterProposalsService;

	@Test
	void givenLoadTransporterProposalsCommand_AndLocale_WhenLoadProposals_ThenReturnTransporterProposals() {
		// given

		TransporterProposalsOutput tpo = JourneyProposalTestData.defaultTransporterProposalsOutput();

		LoadTransporterProposalsCommand command = JourneyProposalTestData
				.defaultLoadTransporterProposalsCommandBuilder().build();

		given(loadTransporterPropsalsPort.loadTransporterProposals(any(LoadTransporterProposalsCriteria.class),
				any(String.class))).willReturn(tpo);

		given(dateTimeHelper.findUserZoneId(any(String.class))).willReturn(ZoneId.of("Africa/Tunis"));

		given(documentUrlResolver.resolveUrl(tpo.getContent().get(0).getVehicule().getImageId(),
				tpo.getContent().get(0).getVehicule().getImageHash())).willReturn("https://vehicule1/photo/url");

		given(documentUrlResolver.resolveUrl(tpo.getContent().get(0).getJourney().getClient().getImageId(),
				tpo.getContent().get(0).getJourney().getClient().getImageHash()))
						.willReturn("https://client1/photo/url");

		LocalDateTime ldt1 = LocalDateTime.of(2022, 01, 01, 10, 30);
		given(dateTimeHelper.systemToUserLocalDateTime(tpo.getContent().get(0).getJourney().getDateTime(),
				ZoneId.of("Africa/Tunis"))).willReturn(ldt1);

		given(documentUrlResolver.resolveUrl(tpo.getContent().get(1).getJourney().getClient().getImageId(),
				tpo.getContent().get(1).getJourney().getClient().getImageHash()))
						.willReturn("https://client2/photo/url");

		given(documentUrlResolver.resolveUrl(tpo.getContent().get(1).getVehicule().getImageId(),
				tpo.getContent().get(1).getVehicule().getImageHash())).willReturn("https://vehicule2/photo/url");

		LocalDateTime ldt2 = LocalDateTime.of(2022, 01, 15, 16, 30);
		given(dateTimeHelper.systemToUserLocalDateTime(tpo.getContent().get(1).getJourney().getDateTime(),
				ZoneId.of("Africa/Tunis"))).willReturn(ldt2);

		// when
		TransporterProposals result = loadTransporterProposalsService.loadProposals(command, "fr_FR");
		// then

		assertEquals(tpo.getTotalPages(), result.getTotalPages());
		assertEquals(tpo.getTotalElements(), result.getTotalElements());
		assertEquals(tpo.getPageSize(), result.getPageSize());
		assertEquals(tpo.getPageNumber(), result.getPageNumber());
		assertEquals(tpo.isHasNext(), result.isHasNext());

		assertEquals(tpo.getContent().size(), result.getContent().size());

		assertEquals(tpo.getContent().get(0).getId(), result.getContent().get(0).getId());
		assertEquals(tpo.getContent().get(0).getPrice(), result.getContent().get(0).getPrice());
		assertEquals(tpo.getContent().get(0).getStatus(), result.getContent().get(0).getStatus());
		
		assertEquals(tpo.getContent().get(0).getVehicule().getId(), result.getContent().get(0).getVehicule().getId());
		assertEquals(tpo.getContent().get(0).getVehicule().getRegsitrationNumber(),
				result.getContent().get(0).getVehicule().getRegsitrationNumber());
		assertEquals(tpo.getContent().get(0).getVehicule().getCirculationDate(),
				result.getContent().get(0).getVehicule().getCirculationDate());
		assertEquals(tpo.getContent().get(0).getVehicule().getConstructorName(),
				result.getContent().get(0).getVehicule().getConstructorName());
		assertEquals(tpo.getContent().get(0).getVehicule().getModelName(),
				result.getContent().get(0).getVehicule().getModelName());

		assertEquals(tpo.getContent().get(0).getVehicule().getEngineTypeId(),
				result.getContent().get(0).getVehicule().getEngineTypeId());
		assertEquals(tpo.getContent().get(0).getVehicule().getEngineTypeName(),
				result.getContent().get(0).getVehicule().getEngineTypeName());

		assertEquals("https://vehicule1/photo/url", result.getContent().get(0).getVehicule().getPhotoUrl());

		assertEquals(tpo.getContent().get(0).getJourney().getId(), result.getContent().get(0).getJourney().getId());
		assertEquals(tpo.getContent().get(0).getJourney().getDistance(),
				result.getContent().get(0).getJourney().getDistance());
		assertEquals(tpo.getContent().get(0).getJourney().getHours(),
				result.getContent().get(0).getJourney().getHours());
		assertEquals(tpo.getContent().get(0).getJourney().getMinutes(),
				result.getContent().get(0).getJourney().getMinutes());
		assertEquals(ldt1, result.getContent().get(0).getJourney().getDateTime());
		assertEquals(tpo.getContent().get(0).getJourney().getWorkers(),
				result.getContent().get(0).getJourney().getWorkers());
		assertEquals(tpo.getContent().get(0).getJourney().getDescription(),
				result.getContent().get(0).getJourney().getDescription());

		assertEquals(tpo.getContent().get(0).getJourney().getDeparturePlace().getId(),
				result.getContent().get(0).getJourney().getDeparturePlace().getId());
		assertEquals(tpo.getContent().get(0).getJourney().getDeparturePlace().getDepartmentId(),
				result.getContent().get(0).getJourney().getDeparturePlace().getDepartmentId());
		assertEquals(tpo.getContent().get(0).getJourney().getDeparturePlace().getType(),
				result.getContent().get(0).getJourney().getDeparturePlace().getType());
		assertEquals(tpo.getContent().get(0).getJourney().getDeparturePlace().getName(),
				result.getContent().get(0).getJourney().getDeparturePlace().getName());
		assertEquals(tpo.getContent().get(0).getJourney().getDeparturePlace().getLongitude(),
				result.getContent().get(0).getJourney().getDeparturePlace().getLongitude());
		assertEquals(tpo.getContent().get(0).getJourney().getDeparturePlace().getLatitude(),
				result.getContent().get(0).getJourney().getDeparturePlace().getLatitude());

		assertEquals(tpo.getContent().get(0).getJourney().getArrivalPlace().getId(),
				result.getContent().get(0).getJourney().getArrivalPlace().getId());
		assertEquals(tpo.getContent().get(0).getJourney().getArrivalPlace().getDepartmentId(),
				result.getContent().get(0).getJourney().getArrivalPlace().getDepartmentId());
		assertEquals(tpo.getContent().get(0).getJourney().getArrivalPlace().getType(),
				result.getContent().get(0).getJourney().getArrivalPlace().getType());
		assertEquals(tpo.getContent().get(0).getJourney().getArrivalPlace().getName(),
				result.getContent().get(0).getJourney().getArrivalPlace().getName());
		assertEquals(tpo.getContent().get(0).getJourney().getArrivalPlace().getLongitude(),
				result.getContent().get(0).getJourney().getArrivalPlace().getLongitude());
		assertEquals(tpo.getContent().get(0).getJourney().getArrivalPlace().getLatitude(),
				result.getContent().get(0).getJourney().getArrivalPlace().getLatitude());

		assertEquals(tpo.getContent().get(0).getJourney().getEngineType().getId(),
				result.getContent().get(0).getJourney().getEngineType().getId());
		assertEquals(tpo.getContent().get(0).getJourney().getEngineType().getName(),
				result.getContent().get(0).getJourney().getEngineType().getName());
		assertEquals(tpo.getContent().get(0).getJourney().getEngineType().getCode(),
				result.getContent().get(0).getJourney().getEngineType().getCode());

		assertEquals(tpo.getContent().get(0).getJourney().getClient().getId(),
				result.getContent().get(0).getJourney().getClient().getId());
		assertEquals(tpo.getContent().get(0).getJourney().getClient().getFirstname(),
				result.getContent().get(0).getJourney().getClient().getFirstname());
		assertEquals("https://client1/photo/url", result.getContent().get(0).getJourney().getClient().getPhotoUrl());

		assertEquals(tpo.getContent().get(1).getId(), result.getContent().get(1).getId());
		assertEquals(tpo.getContent().get(1).getPrice(), result.getContent().get(1).getPrice());

		assertEquals(tpo.getContent().get(1).getVehicule().getId(), result.getContent().get(1).getVehicule().getId());
		assertEquals(tpo.getContent().get(1).getVehicule().getRegsitrationNumber(),
				result.getContent().get(1).getVehicule().getRegsitrationNumber());
		assertEquals(tpo.getContent().get(1).getVehicule().getCirculationDate(),
				result.getContent().get(1).getVehicule().getCirculationDate());
		assertEquals(tpo.getContent().get(1).getVehicule().getConstructorName(),
				result.getContent().get(1).getVehicule().getConstructorName());
		assertEquals(tpo.getContent().get(1).getVehicule().getModelName(),
				result.getContent().get(1).getVehicule().getModelName());

		assertEquals(tpo.getContent().get(1).getVehicule().getEngineTypeId(),
				result.getContent().get(1).getVehicule().getEngineTypeId());
		assertEquals(tpo.getContent().get(1).getVehicule().getEngineTypeName(),
				result.getContent().get(1).getVehicule().getEngineTypeName());

		assertEquals("https://vehicule2/photo/url", result.getContent().get(1).getVehicule().getPhotoUrl());

		assertEquals(tpo.getContent().get(1).getJourney().getId(), result.getContent().get(1).getJourney().getId());
		assertEquals(tpo.getContent().get(1).getJourney().getDistance(),
				result.getContent().get(1).getJourney().getDistance());
		assertEquals(tpo.getContent().get(1).getJourney().getHours(),
				result.getContent().get(1).getJourney().getHours());
		assertEquals(tpo.getContent().get(1).getJourney().getMinutes(),
				result.getContent().get(1).getJourney().getMinutes());
		assertEquals(ldt2, result.getContent().get(1).getJourney().getDateTime());
		assertEquals(tpo.getContent().get(1).getJourney().getWorkers(),
				result.getContent().get(1).getJourney().getWorkers());
		assertEquals(tpo.getContent().get(1).getJourney().getDescription(),
				result.getContent().get(1).getJourney().getDescription());

		assertEquals(tpo.getContent().get(1).getJourney().getDeparturePlace().getId(),
				result.getContent().get(1).getJourney().getDeparturePlace().getId());
		assertEquals(tpo.getContent().get(1).getJourney().getDeparturePlace().getDepartmentId(),
				result.getContent().get(1).getJourney().getDeparturePlace().getDepartmentId());
		assertEquals(tpo.getContent().get(1).getJourney().getDeparturePlace().getType(),
				result.getContent().get(1).getJourney().getDeparturePlace().getType());
		assertEquals(tpo.getContent().get(1).getJourney().getDeparturePlace().getName(),
				result.getContent().get(1).getJourney().getDeparturePlace().getName());
		assertEquals(tpo.getContent().get(1).getJourney().getDeparturePlace().getLongitude(),
				result.getContent().get(1).getJourney().getDeparturePlace().getLongitude());
		assertEquals(tpo.getContent().get(1).getJourney().getDeparturePlace().getLatitude(),
				result.getContent().get(1).getJourney().getDeparturePlace().getLatitude());

		assertEquals(tpo.getContent().get(1).getJourney().getArrivalPlace().getId(),
				result.getContent().get(1).getJourney().getArrivalPlace().getId());
		assertEquals(tpo.getContent().get(1).getJourney().getArrivalPlace().getDepartmentId(),
				result.getContent().get(1).getJourney().getArrivalPlace().getDepartmentId());
		assertEquals(tpo.getContent().get(1).getJourney().getArrivalPlace().getType(),
				result.getContent().get(1).getJourney().getArrivalPlace().getType());
		assertEquals(tpo.getContent().get(1).getJourney().getArrivalPlace().getName(),
				result.getContent().get(1).getJourney().getArrivalPlace().getName());
		assertEquals(tpo.getContent().get(1).getJourney().getArrivalPlace().getLongitude(),
				result.getContent().get(1).getJourney().getArrivalPlace().getLongitude());
		assertEquals(tpo.getContent().get(1).getJourney().getArrivalPlace().getLatitude(),
				result.getContent().get(1).getJourney().getArrivalPlace().getLatitude());

		assertEquals(tpo.getContent().get(1).getJourney().getEngineType().getId(),
				result.getContent().get(1).getJourney().getEngineType().getId());
		assertEquals(tpo.getContent().get(1).getJourney().getEngineType().getName(),
				result.getContent().get(1).getJourney().getEngineType().getName());
		assertEquals(tpo.getContent().get(1).getJourney().getEngineType().getCode(),
				result.getContent().get(1).getJourney().getEngineType().getCode());

		assertEquals(tpo.getContent().get(1).getJourney().getClient().getId(),
				result.getContent().get(1).getJourney().getClient().getId());
		assertEquals(tpo.getContent().get(1).getJourney().getClient().getFirstname(),
				result.getContent().get(1).getJourney().getClient().getFirstname());
		assertEquals("https://client2/photo/url", result.getContent().get(1).getJourney().getClient().getPhotoUrl());

	}

}
