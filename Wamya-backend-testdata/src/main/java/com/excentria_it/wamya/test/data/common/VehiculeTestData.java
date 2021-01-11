package com.excentria_it.wamya.test.data.common;

import java.time.LocalDate;

import com.excentria_it.wamya.application.port.in.AddVehiculeUseCase.AddVehiculeCommand;
import com.excentria_it.wamya.application.port.in.AddVehiculeUseCase.AddVehiculeCommand.AddVehiculeCommandBuilder;
import com.excentria_it.wamya.domain.AddVehiculeDto;
import com.excentria_it.wamya.domain.AddVehiculeDto.AddVehiculeDtoBuilder;

public class VehiculeTestData {
	public static AddVehiculeCommandBuilder defaultAddVehiculeCommandBuilder() {
		return AddVehiculeCommand.builder().constructorId(1L).modelId(1L).circulationDate(LocalDate.of(2020, 01, 01))
				.engineTypeId(1L).registration("0001 TU 220");
	}

	public static AddVehiculeDtoBuilder defaultAddVehiculeDtoBuilder() {
		return AddVehiculeDto.builder().constructorName("PEUGEOT").modelName("PARTNER").engineType("UTILITY")
				.circulationDate(LocalDate.of(2020, 01, 01)).registration("0001 TUN 2020")
				.photoUrl("https://path/to/vehicule/photo");
	}
}
