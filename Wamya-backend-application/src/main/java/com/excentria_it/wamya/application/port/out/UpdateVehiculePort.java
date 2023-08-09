package com.excentria_it.wamya.application.port.out;

public interface UpdateVehiculePort {

	void updateVehiculeImage(String ownerUsername, Long vehiculeId, String location, String hash);

}
