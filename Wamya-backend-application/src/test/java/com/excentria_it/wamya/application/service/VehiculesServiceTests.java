package com.excentria_it.wamya.application.service;

import com.excentria_it.wamya.application.port.in.AddVehiculeUseCase.AddVehicleCommand;
import com.excentria_it.wamya.application.port.in.LoadVehiculesUseCase.LoadVehiculesCommand;
import com.excentria_it.wamya.application.port.out.AddVehiculePort;
import com.excentria_it.wamya.application.port.out.LoadConstructorPort;
import com.excentria_it.wamya.application.port.out.LoadEngineTypePort;
import com.excentria_it.wamya.application.port.out.LoadTransporterVehiculesPort;
import com.excentria_it.wamya.common.exception.ConstructorModelNotFoundException;
import com.excentria_it.wamya.common.exception.ConstructorNotFoundException;
import com.excentria_it.wamya.common.exception.EngineTypeNotFoundException;
import com.excentria_it.wamya.domain.*;
import com.excentria_it.wamya.test.data.common.TestConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.excentria_it.wamya.test.data.common.ConstructorTestData.defaultConstructorDto;
import static com.excentria_it.wamya.test.data.common.EngineTypeTestData.defaultEngineTypeDto;
import static com.excentria_it.wamya.test.data.common.VehiculeTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class VehiculesServiceTests {

    @Mock
    private AddVehiculePort addVehiculePort;

    @Mock
    private LoadConstructorPort loadConstructorPort;

    @Mock
    private LoadEngineTypePort loadEngineTypePort;

    @Mock
    private LoadTransporterVehiculesPort loadTransporterVehiculesPort;

    @Spy
    @InjectMocks
    private VehiculeService vehiculeService;

    @Test
    void testLoadTransporterVehicles() {
        // given
        LoadVehiculesCommand command = defaultLoadVehiculesCommandBuilder().build();
        given(loadTransporterVehiculesPort.loadTransporterVehicules(any(LoadTransporterVehiclesCriteria.class),
                any(String.class)

        )).willReturn(defaultTransporterVehicles());

        // When
        TransporterVehicles result = vehiculeService.loadTransporterVehicules(command, "en_US");

        // Then
        ArgumentCaptor<LoadTransporterVehiclesCriteria> captor = ArgumentCaptor
                .forClass(LoadTransporterVehiclesCriteria.class);

        then(loadTransporterVehiculesPort).should(times(1)).loadTransporterVehicules(captor.capture(), eq("en_US"));

        assertEquals(command.getTransporterUsername(), captor.getValue().getTransporterUsername());


        assertEquals(command.getSortingCriterion().getField(), captor.getValue().getSortingCriterion().getField());

        assertEquals(command.getSortingCriterion().getDirection(),
                captor.getValue().getSortingCriterion().getDirection());

        assertEquals(defaultTransporterVehicles(), result);

    }

    @Test
    void givenNonExistentEngineTypeId_WhenAddVehicle_ThenThrowEngineTypeNotFoundException() {
        // given
        given(loadEngineTypePort.loadEngineTypeById(any(Long.class), any(String.class))).willReturn(Optional.empty());
        AddVehicleCommand command = defaultAddVehiculeCommandBuilder().build();
        // when // then
        assertThrows(EngineTypeNotFoundException.class,
                () -> vehiculeService.addVehicle(command, TestConstants.DEFAULT_EMAIL, "en_US"));
    }

    @Test
    void givenNonExistentConstructorId_WhenAddVehicle_ThenThrowConstructorNotFoundException() {
        // given

        EngineTypeDto engineTypeDto = defaultEngineTypeDto();
        given(loadEngineTypePort.loadEngineTypeById(any(Long.class), any(String.class)))
                .willReturn(Optional.of(engineTypeDto));
        given(loadConstructorPort.loadConstructorById(any(Long.class))).willReturn(Optional.empty());
        AddVehicleCommand command = defaultAddVehiculeCommandBuilder().build();
        // when // then
        assertThrows(ConstructorNotFoundException.class,
                () -> vehiculeService.addVehicle(command, TestConstants.DEFAULT_EMAIL, "en_US"));

    }

    @Test
    void givenNonExistentConstructorModel_WhenAddVehicle_ThenThrowConstructorModelNotFoundException() {
        // given

        EngineTypeDto engineTypeDto = defaultEngineTypeDto();
        given(loadEngineTypePort.loadEngineTypeById(any(Long.class), any(String.class)))
                .willReturn(Optional.of(engineTypeDto));

        ConstructorDto constructorDto = defaultConstructorDto();
        given(loadConstructorPort.loadConstructorById(any(Long.class))).willReturn(Optional.of(constructorDto));

        AddVehicleCommand command = defaultAddVehiculeCommandBuilder().build();
        command.setModelId(3L);
        // when // then
        assertThrows(ConstructorModelNotFoundException.class,
                () -> vehiculeService.addVehicle(command, TestConstants.DEFAULT_EMAIL, "en_US"));

    }

    @Test
    void givenNullConstructorIdAndEmptyConstructorAndModelNames_WhenAddVehicle_ThenThrowConstructorModelNotFoundException() {
        // given

        EngineTypeDto engineTypeDto = defaultEngineTypeDto();
        given(loadEngineTypePort.loadEngineTypeById(any(Long.class), any(String.class)))
                .willReturn(Optional.of(engineTypeDto));

        AddVehicleCommand command = defaultAddVehiculeCommandBuilder().build();
        command.setConstructorId(null);
        // when // then
        assertThrows(ConstructorModelNotFoundException.class,
                () -> vehiculeService.addVehicle(command, TestConstants.DEFAULT_EMAIL, "en_US"));

    }

    @Test
    void givenNullModelIdAndEmptyConstructorAndModelNames_WhenAddVehicle_ThenThrowConstructorModelNotFoundException() {
        // given

        EngineTypeDto engineTypeDto = defaultEngineTypeDto();
        given(loadEngineTypePort.loadEngineTypeById(any(Long.class), any(String.class)))
                .willReturn(Optional.of(engineTypeDto));

        AddVehicleCommand command = defaultAddVehiculeCommandBuilder().build();
        command.setModelId(null);
        // when // then
        assertThrows(ConstructorModelNotFoundException.class,
                () -> vehiculeService.addVehicle(command, TestConstants.DEFAULT_EMAIL, "en_US"));

    }

    @Test
    void givenValidCommandAndConstructorAndModelExist_WhenAddVehicle_ThenSucceed() {
        // given

        EngineTypeDto engineTypeDto = defaultEngineTypeDto();
        given(loadEngineTypePort.loadEngineTypeById(any(Long.class), any(String.class)))
                .willReturn(Optional.of(engineTypeDto));

        ConstructorDto constructorDto = defaultConstructorDto();
        given(loadConstructorPort.loadConstructorById(any(Long.class))).willReturn(Optional.of(constructorDto));

        AddVehicleCommand command = defaultAddVehiculeCommandBuilder().build();

        AddVehiculeDto addVehiculeDto = defaultAddVehiculeDtoBuilder().build();
        given(addVehiculePort.addVehicle(eq(TestConstants.DEFAULT_EMAIL), eq(command.getConstructorId()),
                eq(command.getModelId()), eq(command.getEngineTypeId()), eq(command.getCirculationDate()),
                eq(command.getRegistration()), eq("en_US"))).willReturn(addVehiculeDto);

        // when

        AddVehiculeDto addVehiculeDtoResult = vehiculeService.addVehicle(command, TestConstants.DEFAULT_EMAIL,
                "en_US");
        // then

        then(addVehiculePort).should(times(1)).addVehicle(eq(TestConstants.DEFAULT_EMAIL),
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
    void givenValidCommandAndConstructorAndModelDoesNotExist_WhenAddVehicle_ThenSucceed() {
        // given

        EngineTypeDto engineTypeDto = defaultEngineTypeDto();
        given(loadEngineTypePort.loadEngineTypeById(any(Long.class), any(String.class)))
                .willReturn(Optional.of(engineTypeDto));

        AddVehicleCommand command = defaultAddVehiculeCommandBuilder().build();
        command.setConstructorId(null);
        command.setModelId(null);
        command.setConstructorName("RENAULT");
        command.setModelName("KANGOO");

        AddVehiculeDto addVehiculeDto = defaultAddVehiculeDtoBuilder().build();
        given(addVehiculePort.addVehicle(eq(TestConstants.DEFAULT_EMAIL), eq(command.getConstructorName()),
                eq(command.getModelName()), eq(command.getEngineTypeId()), eq(command.getCirculationDate()),
                eq(command.getRegistration()), eq("en_US"))).willReturn(addVehiculeDto);

        // when

        AddVehiculeDto addVehiculeDtoResult = vehiculeService.addVehicle(command, TestConstants.DEFAULT_EMAIL,
                "en_US");
        // then

        then(addVehiculePort).should(times(1)).addVehicle(eq(TestConstants.DEFAULT_EMAIL),
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
