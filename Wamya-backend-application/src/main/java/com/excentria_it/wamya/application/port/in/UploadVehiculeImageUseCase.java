package com.excentria_it.wamya.application.port.in;

import java.io.BufferedInputStream;

public interface UploadVehiculeImageUseCase {

	void uploadVehiculeImage(BufferedInputStream bufferedInputStream, String originalFilename, Long vehiculeId,
			String name);

}
