package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.domain.LoadTransporterVehiculesCriteria;
import com.excentria_it.wamya.domain.TransporterVehicules;

public interface LoadTransporterVehiculesPort {

	TransporterVehicules loadTransporterVehicules(LoadTransporterVehiculesCriteria criteria, String locale);

}
