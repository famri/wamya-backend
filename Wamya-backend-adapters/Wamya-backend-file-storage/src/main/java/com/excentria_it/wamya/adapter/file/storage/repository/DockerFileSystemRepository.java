package com.excentria_it.wamya.adapter.file.storage.repository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;

import com.excentria_it.wamya.adapter.file.storage.props.DockerFileStorageProperties;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Profile(value = { "file-storage-docker" })
public class DockerFileSystemRepository implements FileRepository {

	private final DockerFileStorageProperties dockerFileStorageProperties;

	@Override
	public String save(InputStream inputStream, String parentFolderName, String fileName) throws IOException {
		Path newFile = Paths.get(dockerFileStorageProperties.getRootFolder() + "/" + parentFolderName + "/"
				+ new Date().getTime() + "-" + fileName);
		Files.createDirectories(newFile.getParent());
		Files.copy(inputStream, newFile);

		return newFile.toAbsolutePath().toString();
	}

	@Override
	public void delete(String fileLocation) throws IOException {
		Path filePath = Paths.get(fileLocation);
		Files.deleteIfExists(filePath);
	}

	@Override
	public FileSystemResource load(String fileLocation) throws IOException {
		return new FileSystemResource(Paths.get(fileLocation));
	}

}