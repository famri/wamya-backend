package com.excentria_it.wamya.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.excentria_it.wamya.application.port.out.CheckUserVehiclePort;
import com.excentria_it.wamya.application.port.out.DeleteFilePort;
import com.excentria_it.wamya.application.port.out.LoadVehiculePort;
import com.excentria_it.wamya.application.port.out.SaveFilePort;
import com.excentria_it.wamya.application.port.out.UpdateVehiculePort;
import com.excentria_it.wamya.application.utils.MimeTypeDetectionFacade;
import com.excentria_it.wamya.common.exception.DocumentAccessException;
import com.excentria_it.wamya.common.exception.ForbiddenAccessException;
import com.excentria_it.wamya.common.exception.UnsupportedMimeTypeException;
import com.excentria_it.wamya.domain.DocumentType;
import com.excentria_it.wamya.test.data.common.TestConstants;

@RunWith(PowerMockRunner.class)
public class VehiculeImageServiceJUnit4Tests {
	@Mock
	private SaveFilePort saveFilePort;
	@Mock
	private DeleteFilePort deleteFilePort;
	@Mock
	private CheckUserVehiclePort checkUserVehiclePort;
	@Mock
	private UpdateVehiculePort updateVehiculePort;
	@Mock
	private LoadVehiculePort loadVehiculePort;

	@InjectMocks
	private VehicleImageService vehiculeImageService;

	@Test
	public void givenNotCurrentUserVehicule_whenUploadVehiculeImage_thenThrowForbiddenAccessException() {
		// given
		given(checkUserVehiclePort.isUserVehicle(any(String.class), any(Long.class))).willReturn(false);

		byte[] initialArray = { 0, 1, 2 };
		BufferedInputStream imageInputStream = new BufferedInputStream(new ByteArrayInputStream(initialArray));

		// When // then

		assertThrows(ForbiddenAccessException.class, () -> vehiculeImageService.uploadVehicleImage(imageInputStream,
				"Image.jpg", 1L, TestConstants.DEFAULT_EMAIL));

	}

	@Test
	@PrepareForTest(MimeTypeDetectionFacade.class)
	public void givenBadDocumentType_whenUploadVehiculeImage_thenThrowUnsupportedMimeTypeException()
			throws IOException {
		// given
		given(checkUserVehiclePort.isUserVehicle(any(String.class), any(Long.class))).willReturn(true);

		byte[] initialArray = { 0, 1, 2 };
		BufferedInputStream imageInputStream = new BufferedInputStream(new ByteArrayInputStream(initialArray));
		PowerMockito.mockStatic(MimeTypeDetectionFacade.class);
		PowerMockito.when(MimeTypeDetectionFacade.detectContentType(any(InputStream.class)))
				.thenReturn("application/pdf");

		// When // then

		assertThrows(UnsupportedMimeTypeException.class, () -> vehiculeImageService
				.uploadVehicleImage(imageInputStream, "Image.jpg", 1L, TestConstants.DEFAULT_EMAIL));

	}

	@Test
	@PrepareForTest(MimeTypeDetectionFacade.class)
	public void givenIOExceptionWhenDetectingDocumentType_whenUploadVehiculeImage_thenThrowDocumentAccessException()
			throws IOException {
		// given
		given(checkUserVehiclePort.isUserVehicle(any(String.class), any(Long.class))).willReturn(true);

		byte[] initialArray = { 0, 1, 2 };
		BufferedInputStream imageInputStream = new BufferedInputStream(new ByteArrayInputStream(initialArray));
		PowerMockito.mockStatic(MimeTypeDetectionFacade.class);
		PowerMockito.when(MimeTypeDetectionFacade.detectContentType(any(InputStream.class)))
				.thenThrow(new IOException("Unable to read from file input stream."));

		// When // then

		assertThrows(DocumentAccessException.class, () -> vehiculeImageService.uploadVehicleImage(imageInputStream,
				"Image.jpg", 1L, TestConstants.DEFAULT_EMAIL));

	}

	@Test
	@PrepareForTest(MimeTypeDetectionFacade.class)
	public void givenIOExceptionWhenSavingFile_whenUploadVehiculeImage_thenThrowDocumentAccessException()
			throws IOException {
		given(checkUserVehiclePort.isUserVehicle(any(String.class), any(Long.class))).willReturn(true);
		// given
		byte[] initialArray = { 0, 1, 2 };
		BufferedInputStream imageInputStream = new BufferedInputStream(new ByteArrayInputStream(initialArray));

		doThrow(IOException.class).when(saveFilePort).saveFile(imageInputStream,
				DocumentType.IMAGE_JPEG.getParentFolder(), "Image.jpg");

		PowerMockito.mockStatic(MimeTypeDetectionFacade.class);
		PowerMockito.when(MimeTypeDetectionFacade.detectContentType(any(InputStream.class))).thenReturn("image/jpeg");

		// when // then
		assertThrows(DocumentAccessException.class, () -> vehiculeImageService.uploadVehicleImage(imageInputStream,
				"Image.jpg", 1L, TestConstants.DEFAULT_EMAIL));

	}
	
	@Test
	@PrepareForTest(MimeTypeDetectionFacade.class)
	public void givenIOExceptionWhenDeletingFile_whenUploadVehiculeImage_thenThrowDocumentAccessException()
			throws IOException {
		given(checkUserVehiclePort.isUserVehicle(any(String.class), any(Long.class))).willReturn(true);
		// given
		byte[] initialArray = { 0, 1, 2 };
		BufferedInputStream imageInputStream = new BufferedInputStream(new ByteArrayInputStream(initialArray));
		
		Long currentTime = new Date().getTime();
		given(saveFilePort.saveFile(imageInputStream, DocumentType.IMAGE_JPEG.getParentFolder(), "Image.jpg"))
				.willReturn("/Images/" + currentTime + "-Image.jpg");

		PowerMockito.mockStatic(MimeTypeDetectionFacade.class);
		PowerMockito.when(MimeTypeDetectionFacade.detectContentType(any(InputStream.class))).thenReturn("image/jpeg");
		
		given(loadVehiculePort.hasDefaultVehiculeImage(any(Long.class))).willReturn(false);
		given(loadVehiculePort.loadImageLocation(any(Long.class)))
				.willReturn("/Images/" + (currentTime - 60000) + "-Image-old.jpg");

		doThrow(IOException.class).when(deleteFilePort).deleteFile(any(String.class));

		// when // then
		assertThrows(DocumentAccessException.class, () -> vehiculeImageService.uploadVehicleImage(imageInputStream,
				"Image.jpg", 1L, TestConstants.DEFAULT_EMAIL));

	}

	@Test
	@PrepareForTest(MimeTypeDetectionFacade.class)
	public void givenNonDefaultVehiculeImage_whenUploadVehiculeImage_ThenCheckUploadedFileTypeThenSaveUploadedFileThenDeleteOldVehiculeImage()
			throws IOException {

		// given
		byte[] initialArray = { 0, 1, 2 };
		BufferedInputStream imageInputStream = new BufferedInputStream(new ByteArrayInputStream(initialArray));

		given(checkUserVehiclePort.isUserVehicle(any(String.class), any(Long.class))).willReturn(true);
		
		PowerMockito.mockStatic(MimeTypeDetectionFacade.class);
		PowerMockito.when(MimeTypeDetectionFacade.detectContentType(any(InputStream.class))).thenReturn("image/jpeg");
		
		Long currentTime = new Date().getTime();
		given(saveFilePort.saveFile(imageInputStream, DocumentType.IMAGE_JPEG.getParentFolder(), "Image.jpg"))
				.willReturn("/Images/" + currentTime + "-Image.jpg");
	

		given(loadVehiculePort.hasDefaultVehiculeImage(any(Long.class))).willReturn(false);
		given(loadVehiculePort.loadImageLocation(any(Long.class)))
				.willReturn("/Images/" + (currentTime - 60000) + "-Image-old.jpg");

		

		// when
		vehiculeImageService.uploadVehicleImage(imageInputStream, "Image.jpg", 1L, TestConstants.DEFAULT_EMAIL);

		// then
		PowerMockito.verifyStatic(MimeTypeDetectionFacade.class, times(1));
		MimeTypeDetectionFacade.detectContentType(imageInputStream);

		then(saveFilePort).should(times(1)).saveFile(imageInputStream, DocumentType.IMAGE_JPEG.getParentFolder(),
				"Image.jpg");
		then(updateVehiculePort).should(times(1)).updateVehiculeImage(TestConstants.DEFAULT_EMAIL,1L,
				"/Images/" + currentTime + "-Image.jpg",
				DigestUtils.sha256Hex("/Images/" + currentTime + "-Image.jpg"));

		then(deleteFilePort).should(times(1)).deleteFile("/Images/" + (currentTime - 60000) + "-Image-old.jpg");
	}

	@Test
	@PrepareForTest(MimeTypeDetectionFacade.class)
	public void givenDefaultCurrentProfileImage_whenUploadProfileImage_ThenCheckUploadedFileTypeThenSaveUploadedFileThenDoNotDeleteOldProfileImage()
			throws IOException {

		// given
		byte[] initialArray = { 0, 1, 2 };
		BufferedInputStream imageInputStream = new BufferedInputStream(new ByteArrayInputStream(initialArray));

		given(checkUserVehiclePort.isUserVehicle(any(String.class), any(Long.class))).willReturn(true);
		
		PowerMockito.mockStatic(MimeTypeDetectionFacade.class);
		PowerMockito.when(MimeTypeDetectionFacade.detectContentType(any(InputStream.class))).thenReturn("image/jpeg");
		
		Long currentTime = new Date().getTime();
		given(saveFilePort.saveFile(imageInputStream, DocumentType.IMAGE_JPEG.getParentFolder(), "Image.jpg"))
				.willReturn("/Images/" + currentTime + "-Image.jpg");
	

		given(loadVehiculePort.hasDefaultVehiculeImage(any(Long.class))).willReturn(true);
		

		

		// when
		vehiculeImageService.uploadVehicleImage(imageInputStream, "Image.jpg", 1L, TestConstants.DEFAULT_EMAIL);

		// then
		PowerMockito.verifyStatic(MimeTypeDetectionFacade.class, times(1));
		MimeTypeDetectionFacade.detectContentType(imageInputStream);

		then(saveFilePort).should(times(1)).saveFile(imageInputStream, DocumentType.IMAGE_JPEG.getParentFolder(),
				"Image.jpg");
		then(updateVehiculePort).should(times(1)).updateVehiculeImage(TestConstants.DEFAULT_EMAIL,1L,
				"/Images/" + currentTime + "-Image.jpg",
				DigestUtils.sha256Hex("/Images/" + currentTime + "-Image.jpg"));

		then(deleteFilePort).should(never()).deleteFile("/Images/" + (currentTime - 60000) + "-Image-old.jpg");
	}
}
