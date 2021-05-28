package com.excentria_it.wamya.adapter.file.storage.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.adapter.file.storage.repository.FileRepository;

@ExtendWith(MockitoExtension.class)
public class FileStorageAdapterTests {

	@Mock
	private FileRepository fileRepository;

	@InjectMocks
	private FileStorageAdapter fileStorageAdapter;

	@Test
	void givenNoException_whenSaveFile_thenReturnSavedFileLocation() throws IOException {
		// given

		InputStream is = FileStorageAdapterTests.class.getResourceAsStream("/Image.jpg");

		String expectedLocation = "/some/location/image.jpg";
		given(fileRepository.save(any(InputStream.class), any(String.class), any(String.class)))
				.willReturn(expectedLocation);

		// when
		String location = fileStorageAdapter.saveFile(is, "Images", "my_profile_image");
		// then
		then(fileRepository).should(times(1)).save(is, "Images", "my_profile_image");
		assertEquals(expectedLocation, location);
	}

	@Test
	void givenNoException_whenDeleteFile_thenReturnCallRepositoryDelete() throws IOException {
		// given

		String fileLocation = "/some/location/image.jpg";

		// when
		fileStorageAdapter.deleteFile(fileLocation);
		// then
		then(fileRepository).should(times(1)).delete(fileLocation);

	}
	
	@Test
	void givenNoException_whenLoadFile_thenReturnCallRepositoryLoad () throws IOException {
		// given

		String fileLocation = "/some/location/image.jpg";

		// when
		fileStorageAdapter.loadFile(fileLocation);
		// then
		then(fileRepository).should(times(1)).load(fileLocation);

	}

}
