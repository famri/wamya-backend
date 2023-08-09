package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.domain.LoadTransporterVehiclesCriteria;
import com.excentria_it.wamya.domain.TransporterVehicles;

public interface LoadTransporterVehiculesPort {

	TransporterVehicles loadTransporterVehicules(LoadTransporterVehiclesCriteria criteria, String locale);

}
