package com.excentria_it.wamya.application.port.in;

import java.io.BufferedInputStream;

public interface UploadProfileImageUseCase {

	void uploadProfileImage(BufferedInputStream imageInputStream, String imageOriginalName, String username);

}
