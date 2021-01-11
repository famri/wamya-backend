package com.excentria_it.wamya.application.service;

import static com.excentria_it.wamya.test.data.common.ConstructorTestData.*;
import static com.excentria_it.wamya.test.data.common.EngineTypeTestData.*;
import static com.excentria_it.wamya.test.data.common.VehiculeTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.in.AddVehiculeUseCase.AddVehiculeCommand;
import com.excentria_it.wamya.application.port.out.AddVehiculePort;
import com.excentria_it.wamya.application.port.out.LoadConstructorPort;
import com.excentria_it.wamya.application.port.out.LoadEngineTypePort;
import com.excentria_it.wamya.common.exception.ConstructorModelNotFoundException;
import com.excentria_it.wamya.common.exception.ConstructorNotFoundException;
import com.excentria_it.wamya.common.exception.EngineTypeNotFoundException;
import com.excentria_it.wamya.domain.AddVehiculeDto;
import com.excentria_it.wamya.domain.ConstructorDto;
import com.excentria_it.wamya.domain.EngineTypeDto;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ExtendWith(MockitoExtension.class)
public class VehiculesServiceTests {

	@Mock
	private AddVehiculePort addVehiculePort;

	@Mock
	private LoadConstructorPort loadConstructorPort;

	@Mock
	private LoadEngineTypePort loadEngineTypePort;

	@Spy
	@InjectMocks
	private VehiculeService vehiculeService;

	@Test
	void givenInexistentEngineTypeId_WhenAddVehicule_ThenThrowEngineTypeNotFoundException() {
		// given
		given(loadEngineTypePort.loadEngineTypeById(any(Long.class), any(String.class))).willReturn(Optional.empty());
		AddVehiculeCommand command = defaultAddVehiculeCommandBuilder().build();
		// when // then
		assertThrows(EngineTypeNotFoundException.class,
				() -> vehiculeService.addVehicule(command, TestConstants.DEFAULT_EMAIL, "en_US"));
	}

	@Test
	void givenInexistentConstructorId_WhenAddVehicule_ThenThrowConstructorNotFoundException() {
		// given

		EngineTypeDto engineTypeDto = defaultEngineTypeDto();
		given(loadEngineTypePort.loadEngineTypeById(any(Long.class), any(String.class)))
				.willReturn(Optional.of(engineTypeDto));
		given(loadConstructorPort.loadConstructorById(any(Long.class))).willReturn(Optional.empty());
		AddVehiculeCommand command = defaultAddVehiculeCommandBuilder().build();
		// when // then
		assertThrows(ConstructorNotFoundException.class,
				() -> vehiculeService.addVehicule(command, TestConstants.DEFAULT_EMAIL, "en_US"));

	}

	@Test
	void givenInexistentConstructorModel_WhenAddVehicule_ThenThrowConstructorModelNotFoundException() {
		// given

		EngineTypeDto engineTypeDto = defaultEngineTypeDto();
		given(loadEngineTypePort.loadEngineTypeById(any(Long.class), any(String.class)))
				.willReturn(Optional.of(engineTypeDto));

		ConstructorDto constructorDto = defaultConstructorDto();
		given(loadConstructorPort.loadConstructorById(any(Long.class))).willReturn(Optional.of(constructorDto));

		AddVehiculeCommand command = defaultAddVehiculeCommandBuilder().build();
		command.setModelId(3L);
		// when // then
		assertThrows(ConstructorModelNotFoundException.class,
				() -> vehiculeService.addVehicule(command, TestConstants.DEFAULT_EMAIL, "en_US"));

	}

	@Test
	void givenNullConstructorIdAndEmptyConstructorAndModelNames_WhenAddVehicule_ThenThrowConstructorModelNotFoundException() {
		// given

		EngineTypeDto engineTypeDto = defaultEngineTypeDto();
		given(loadEngineTypePort.loadEngineTypeById(any(Long.class), any(String.class)))
				.willReturn(Optional.of(engineTypeDto));

		AddVehiculeCommand command = defaultAddVehiculeCommandBuilder().build();
		command.setConstructorId(null);
		// when // then
		assertThrows(ConstructorModelNotFoundException.class,
				() -> vehiculeService.addVehicule(command, TestConstants.DEFAULT_EMAIL, "en_US"));

	}

	@Test
	void givenNullModelIdAndEmptyConstructorAndModelNames_WhenAddVehicule_ThenThrowConstructorModelNotFoundException() {
		// given

		EngineTypeDto engineTypeDto = defaultEngineTypeDto();
		given(loadEngineTypePort.loadEngineTypeById(any(Long.class), any(String.class)))
				.willReturn(Optional.of(engineTypeDto));

		AddVehiculeCommand command = defaultAddVehiculeCommandBuilder().build();
		command.setModelId(null);
		// when // then
		assertThrows(ConstructorModelNotFoundException.class,
				() -> vehiculeService.addVehicule(command, TestConstants.DEFAULT_EMAIL, "en_US"));

	}

	@Test
	void givenValidCommandAndConstructorAndModelExist_WhenAddVehicule_ThenSucceed() {
		// given

		EngineTypeDto engineTypeDto = defaultEngineTypeDto();
		given(loadEngineTypePort.loadEngineTypeById(any(Long.class), any(String.class)))
				.willReturn(Optional.of(engineTypeDto));

		ConstructorDto constructorDto = defaultConstructorDto();
		given(loadConstructorPort.loadConstructorById(any(Long.class))).willReturn(Optional.of(constructorDto));

		AddVehiculeCommand command = defaultAddVehiculeCommandBuilder().build();

		AddVehiculeDto addVehiculeDto = defaultAddVehiculeDtoBuilder().build();
		given(addVehiculePort.addVehicule(eq(TestConstants.DEFAULT_EMAIL), eq(command.getConstructorId()),
				eq(command.getModelId()), eq(command.getEngineTypeId()), eq(command.getCirculationDate()),
				eq(command.getRegistration()), eq("en_US"))).willReturn(addVehiculeDto);

		// when

		AddVehiculeDto addVehiculeDtoResult = vehiculeService.addVehicule(command, TestConstants.DEFAULT_EMAIL,
				"en_US");
		// then

		then(addVehiculePort).should(times(1)).addVehicule(eq(TestConstants.DEFAULT_EMAIL),
				eq(command.getConstructorId()), eq(command.getModelId()), eq(command.getEngineTypeId()),
				eq(command.getCirculationDate()), eq(command.getRegistration()), eq("en_US"));

		assertEquals(addVehiculeDto.getConstructorName(), addVehiculeDtoResult.getConstructorName());
		assertEquals(addVehiculeDto.getModelName(), addVehiculeDtoResult.getModelName());
		assertEquals(addVehiculeDto.getRegistration(), addVehiculeDtoResult.getRegistration());
		assertEquals(addVehiculeDto.getCirculationDate(), addVehiculeDtoResult.getCirculationDate());
		assertEquals(addVehiculeDto.getPhotoUrl(), addVehiculeDtoResult.getPhotoUrl());
		assertEquals(addVehiculeDto.getEngineType(), addVehiculeDtoResult.getEngineType());

	}

	@Test
	void givenValidCommandAndConstructorAndModelDontExist_WhenAddVehicule_ThenSucceed() {
		// given

		EngineTypeDto engineTypeDto = defaultEngineTypeDto();
		given(loadEngineTypePort.loadEngineTypeById(any(Long.class), any(String.class)))
				.willReturn(Optional.of(engineTypeDto));

		AddVehiculeCommand command = defaultAddVehiculeCommandBuilder().build();
		command.setConstructorId(null);
		command.setModelId(null);
		command.setConstructorName("RENAULT");
		command.setModelName("KANGOO");

		AddVehiculeDto addVehiculeDto = defaultAddVehiculeDtoBuilder().build();
		given(addVehiculePort.addVehicule(eq(TestConstants.DEFAULT_EMAIL), eq(command.getConstructorName()),
				eq(command.getModelName()), eq(command.getEngineTypeId()), eq(command.getCirculationDate()),
				eq(command.getRegistration()), eq("en_US"))).willReturn(addVehiculeDto);

		// when

		AddVehiculeDto addVehiculeDtoResult = vehiculeService.addVehicule(command, TestConstants.DEFAULT_EMAIL,
				"en_US");
		// then

		then(addVehiculePort).should(times(1)).addVehicule(eq(TestConstants.DEFAULT_EMAIL),
				eq(command.getConstructorName()), eq(command.getModelName()), eq(command.getEngineTypeId()),
				eq(command.getCirculationDate()), eq(command.getRegistration()), eq("en_US"));

		assertEquals(addVehiculeDto.getConstructorName(), addVehiculeDtoResult.getConstructorName());
		assertEquals(addVehiculeDto.getModelName(), addVehiculeDtoResult.getModelName());
		assertEquals(addVehiculeDto.getRegistration(), addVehiculeDtoResult.getRegistration());
		assertEquals(addVehiculeDto.getCirculationDate(), addVehiculeDtoResult.getCirculationDate());
		assertEquals(addVehiculeDto.getPhotoUrl(), addVehiculeDtoResult.getPhotoUrl());
		assertEquals(addVehiculeDto.getEngineType(), addVehiculeDtoResult.getEngineType());

	}

}
