package com.excentria_it.wamya.adapter.file.storage.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.FileSystemResource;

import com.excentria_it.wamya.adapter.file.storage.props.LocalFileStorageProperties;

@ExtendWith(MockitoExtension.class)
public class LocalFileSystemRepositoryTests {
	@Mock
	private LocalFileStorageProperties localFileStorageProperties;

	@InjectMocks
	private LocalFileSystemRepository localFileSystemRepository;

	@TempDir
	Path testDir;

//	@Rule
//	public TemporaryFolder tempFolder = new TemporaryFolder();

	@Test
	void testSave() throws IOException {
		// given

		Long startTime = new Date().getTime();
		InputStream is = LocalFileSystemRepositoryTests.class.getResourceAsStream("/Image.jpg");

		given(localFileStorageProperties.getRootFolder()).willReturn(testDir.toAbsolutePath().toString());
		// When
		String location = localFileSystemRepository.save(is, "Images", "my_profile_image.jpg");

		// then
		assertTrue(Files.exists(Paths.get(location)));

		assertTrue(Files.getLastModifiedTime(Paths.get(location)).toMillis() > startTime);
	}

	@Test
	void testDelete() throws IOException {
		// given

		Path filePath = Paths.get(testDir.toAbsolutePath().toString() + "/test_delete.txt");
		Path createdFilePath = Files.createFile(filePath);
		assertTrue(Files.exists(createdFilePath));

		// When
		localFileSystemRepository.delete(createdFilePath.toAbsolutePath().toString());
		// then

		assertFalse(Files.exists(createdFilePath));

	}

	@Test
	void testLoad() throws IOException {
		Path filePath = Paths.get(testDir.toAbsolutePath().toString() + "/test_delete.txt");
		Path createdFilePath = Files.createFile(filePath);
		assertTrue(Files.exists(createdFilePath));

		// When
		FileSystemResource res = localFileSystemRepository.load(createdFilePath.toAbsolutePath().toString());
		// then

		assertTrue(IOUtils.contentEquals(Files.newInputStream(filePath), res.getInputStream()));

	}
}
