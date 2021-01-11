package com.excentria_it.wamya.application.port.out;

import java.time.LocalDate;

import com.excentria_it.wamya.domain.AddVehiculeDto;

public interface AddVehiculePort {

	AddVehiculeDto addVehicule(String transporterUsername, Long constructorId, Long modelId, Long engineTypeId,
			LocalDate circulationDate, String registration, String locale);

	AddVehiculeDto addVehicule(String transporterUsername, String constructorName, String modelName, Long engineTypeId,
			LocalDate circulationDate, String registration, String locale);
}
