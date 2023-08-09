package com.excentria_it.wamya.application.port.out;

import java.io.IOException;

public interface DeleteFilePort {

	void deleteFile(String currentAvatarFileLocation) throws IOException;

}
