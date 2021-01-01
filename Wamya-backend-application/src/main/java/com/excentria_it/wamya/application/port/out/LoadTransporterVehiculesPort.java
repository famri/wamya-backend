package com.excentria_it.wamya.application.port.out;

import java.util.Set;

import com.excentria_it.wamya.domain.JourneyProposalDto.VehiculeDto;

public interface LoadTransporterVehiculesPort {

	Set<VehiculeDto> loadTransporterVehicules(String transporterEmail);

	Set<VehiculeDto> loadTransporterVehicules(String transporterIcc, String transporterMobileNumber);
}
