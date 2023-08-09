package com.excentria_it.wamya.application.port.out;

public interface LoadVehiculePort {

	String loadImageLocation(Long vehiculeId);

	boolean hasDefaultVehiculeImage(Long vehiculeId);

}
