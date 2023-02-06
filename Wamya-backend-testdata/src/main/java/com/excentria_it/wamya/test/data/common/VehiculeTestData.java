package com.excentria_it.wamya.test.data.common;

import com.excentria_it.wamya.application.port.in.AddVehiculeUseCase.AddVehicleCommand;
import com.excentria_it.wamya.application.port.in.LoadVehiculesUseCase.LoadVehiculesCommand;
import com.excentria_it.wamya.application.port.in.LoadVehiculesUseCase.LoadVehiculesCommand.LoadVehiculesCommandBuilder;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.domain.AddVehiculeDto;
import com.excentria_it.wamya.domain.AddVehiculeDto.AddVehiculeDtoBuilder;
import com.excentria_it.wamya.domain.TransporterVehicleDto;
import com.excentria_it.wamya.domain.TransporterVehicles;

import java.time.LocalDate;
import java.util.List;

public class VehiculeTestData {

    private static final List<TransporterVehicleDto> DEFAULT_TRANSPORTER_VEHICLE_DTOS = List.of(
            TransporterVehicleDto.builder().id(1L).registrationNumber("0001 TUN 220")
                    .circulationDate(LocalDate.of(2020, 01, 01)).constructorName("PEUGEOT").modelName("PARTNER")
                    .engineTypeName("Utility vehicule").engineTypeId(1L).photoUrl("https://some/photo/url/1").build(),

            TransporterVehicleDto.builder().id(2L).registrationNumber("0002 TUN 220")
                    .circulationDate(LocalDate.of(2020, 01, 02)).constructorName("CITROEN").modelName("NEMO")
                    .engineTypeName("Utility vehicule").engineTypeId(1L).photoUrl("https://some/photo/url/2").build());

    public static LoadVehiculesCommandBuilder defaultLoadVehiculesCommandBuilder() {
        return LoadVehiculesCommand.builder().transporterUsername(TestConstants.DEFAULT_EMAIL)
                .sortingCriterion(new SortCriterion("id", "asc"));
    }

    public static TransporterVehicles defaultTransporterVehicles() {
        return TransporterVehicles.builder().content(DEFAULT_TRANSPORTER_VEHICLE_DTOS).build();
    }

    public static AddVehicleCommand.AddVehicleCommandBuilder defaultAddVehiculeCommandBuilder() {
        return AddVehicleCommand.builder().constructorId(1L).modelId(1L).circulationDate(LocalDate.of(2020, 01, 01))
                .engineTypeId(1L).registration("0001 TU 220");
    }

    public static AddVehiculeDtoBuilder defaultAddVehiculeDtoBuilder() {
        return AddVehiculeDto.builder().constructorName("PEUGEOT").modelName("PARTNER").engineType("UTILITY")
                .circulationDate(LocalDate.of(2020, 01, 01)).registration("0001 TUN 2020")
                .photoUrl("https://path/to/vehicule/photo");
    }

}
