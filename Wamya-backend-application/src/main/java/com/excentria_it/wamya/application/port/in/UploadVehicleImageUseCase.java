package com.excentria_it.wamya.application.port.in;

import java.io.BufferedInputStream;

public interface UploadVehicleImageUseCase {

    void uploadVehicleImage(BufferedInputStream bufferedInputStream, String originalFilename, Long vehicleId,
                            String name);

}
