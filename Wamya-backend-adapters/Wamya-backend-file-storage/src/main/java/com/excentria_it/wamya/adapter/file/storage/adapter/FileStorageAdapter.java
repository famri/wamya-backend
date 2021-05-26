package com.excentria_it.wamya.adapter.file.storage.adapter;

import java.io.IOException;
import java.io.InputStream;

import com.excentria_it.wamya.adapter.file.storage.repository.FileRepository;
import com.excentria_it.wamya.application.port.out.DeleteFilePort;
import com.excentria_it.wamya.application.port.out.SaveFilePort;
import com.excentria_it.wamya.common.annotation.StorageAdapter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@StorageAdapter
public class FileStorageAdapter implements SaveFilePort, DeleteFilePort {

	private final FileRepository fileRepository;

	@Override
	public String saveFile(InputStream inputStream, String parentFolderName, String fileName) throws IOException {
		try {
			return fileRepository.save(inputStream, parentFolderName, fileName);
		} finally {
			inputStream.close();
		}

	}

	@Override
	public void deleteFile(String fileLocation) throws IOException {

		fileRepository.delete(fileLocation);

	}

}
