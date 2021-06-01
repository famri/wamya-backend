package com.excentria_it.wamya.application.port.in;

import java.io.BufferedInputStream;

public interface UploadIdentityDocumentUseCase {

	void uploadIdentityDocument(BufferedInputStream bufferedInputStream, String originalFilename, String name);

}
