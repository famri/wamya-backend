package com.excentria_it.wamya.adapter.file.storage.repository;

import java.io.IOException;
import java.io.InputStream;

public interface FileRepository {
	String save(InputStream inputStream, String parentFolderName, String fileName) throws IOException;

	void delete(String fileLocation) throws IOException;
}
