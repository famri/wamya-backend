package com.excentria_it.wamya.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.excentria_it.wamya.application.port.out.DeleteFilePort;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.SaveFilePort;
import com.excentria_it.wamya.application.port.out.UpdateUserAccountPort;
import com.excentria_it.wamya.application.utils.MimeTypeDetectionFacade;
import com.excentria_it.wamya.common.exception.DocumentAccessException;
import com.excentria_it.wamya.common.exception.UnsupportedMimeTypeException;
import com.excentria_it.wamya.domain.DocumentType;
import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.domain.ValidationState;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.excentria_it.wamya.test.data.common.UserAccountTestData;

@RunWith(PowerMockRunner.class)
public class IndentityDocumentServiceJunit4Tests {
	@Mock
	private SaveFilePort saveFilePort;
	@Mock
	private DeleteFilePort deleteFilePort;
	@Mock
	private LoadUserAccountPort loadUserAccountPort;
	@Mock
	private UpdateUserAccountPort updateUserAccountPort;

	@InjectMocks
	private IdentityDocumentService identityDocumentService;

	@Test
	@PrepareForTest(MimeTypeDetectionFacade.class)
	public void givenExistentIdentityDocument_whenUploadIdentityDocument_ThenCheckUploadedFileTypeThenSaveUploadedFileThenDeleteOldIdentityDocument()
			throws IOException {

		// given
		byte[] initialArray = { 0, 1, 2 };
		BufferedInputStream documentInputStream = new BufferedInputStream(new ByteArrayInputStream(initialArray));

		Long currentTime = new Date().getTime();
		given(saveFilePort.saveFile(documentInputStream, DocumentType.IMAGE_JPEG.getParentFolder(), "Image.jpg"))
				.willReturn("/Images/" + currentTime + "-Image.jpg");

		UserAccount userAccount = UserAccountTestData.defaultUserAccountBuilder().build();
		given(loadUserAccountPort.loadUserAccountByUsername(TestConstants.DEFAULT_EMAIL))
				.willReturn(Optional.of(userAccount));

		given(loadUserAccountPort.hasNoIdentityImage(userAccount.getId())).willReturn(false);
		given(loadUserAccountPort.loadIdentityDocumentLocation(userAccount.getId()))
				.willReturn("/Images/" + (currentTime - 60000) + "-Image-old.jpg");

		PowerMockito.mockStatic(MimeTypeDetectionFacade.class);
		PowerMockito.when(MimeTypeDetectionFacade.detectContentType(any(InputStream.class))).thenReturn("image/jpeg");

		// when
		identityDocumentService.uploadIdentityDocument(documentInputStream, "Image.jpg", TestConstants.DEFAULT_EMAIL);

		// then
		PowerMockito.verifyStatic(MimeTypeDetectionFacade.class, times(1));
		MimeTypeDetectionFacade.detectContentType(documentInputStream);

		then(saveFilePort).should(times(1)).saveFile(documentInputStream, DocumentType.IMAGE_JPEG.getParentFolder(),
				"Image.jpg");
		then(updateUserAccountPort).should(times(1)).updateIdentityDocument(userAccount.getId(),
				"/Images/" + currentTime + "-Image.jpg", DigestUtils.sha256Hex("/Images/" + currentTime + "-Image.jpg"),
				DocumentType.IMAGE_JPEG);

		then(deleteFilePort).should(times(1)).deleteFile("/Images/" + (currentTime - 60000) + "-Image-old.jpg");
	}

	@Test
	@PrepareForTest(MimeTypeDetectionFacade.class)
	public void givenInexistentIdentityDocument_whenUploadIdentityDocument_ThenCheckUploadedFileTypeThenSaveUploadedFile()
			throws IOException {

		// given
		byte[] initialArray = { 0, 1, 2 };
		BufferedInputStream documentInputStream = new BufferedInputStream(new ByteArrayInputStream(initialArray));

		Long currentTime = new Date().getTime();
		given(saveFilePort.saveFile(documentInputStream, DocumentType.APPLICATION_PDF.getParentFolder(),
				"Identity.pdf")).willReturn("/Documents/" + currentTime + "-Identity.pdf");

		UserAccount userAccount = UserAccountTestData.defaultUserAccountBuilder().build();
		given(loadUserAccountPort.loadUserAccountByUsername(TestConstants.DEFAULT_EMAIL))
				.willReturn(Optional.of(userAccount));

		given(loadUserAccountPort.hasNoIdentityImage(userAccount.getId())).willReturn(true);

		PowerMockito.mockStatic(MimeTypeDetectionFacade.class);
		PowerMockito.when(MimeTypeDetectionFacade.detectContentType(any(InputStream.class)))
				.thenReturn("application/pdf");

		// when
		identityDocumentService.uploadIdentityDocument(documentInputStream, "Identity.pdf",
				TestConstants.DEFAULT_EMAIL);

		// then
		PowerMockito.verifyStatic(MimeTypeDetectionFacade.class, times(1));
		MimeTypeDetectionFacade.detectContentType(documentInputStream);

		then(saveFilePort).should(times(1)).saveFile(documentInputStream,
				DocumentType.APPLICATION_PDF.getParentFolder(), "Identity.pdf");

		then(updateUserAccountPort).should(times(1)).updateIdentityDocument(userAccount.getId(),
				"/Documents/" + currentTime + "-Identity.pdf",
				DigestUtils.sha256Hex("/Documents/" + currentTime + "-Identity.pdf"), DocumentType.APPLICATION_PDF);
		then(updateUserAccountPort).should(times(1)).updateIdentityValidationState(userAccount.getId(),
				ValidationState.PENDING);

		then(deleteFilePort).should(never()).deleteFile(any(String.class));
	}

	@Test
	@PrepareForTest(MimeTypeDetectionFacade.class)
	public void givenBadDocumentType_whenUploadProfileImage_ThenThrowUnsupportedMimeTypeException() throws IOException {

		// given
		byte[] initialArray = { 0, 1, 2 };
		BufferedInputStream imageInputStream = new BufferedInputStream(new ByteArrayInputStream(initialArray));

		PowerMockito.mockStatic(MimeTypeDetectionFacade.class);
		PowerMockito.when(MimeTypeDetectionFacade.detectContentType(any(InputStream.class)))
				.thenReturn("application/png");

		// when
		assertThrows(UnsupportedMimeTypeException.class, () -> identityDocumentService
				.uploadIdentityDocument(imageInputStream, "Image.png", TestConstants.DEFAULT_EMAIL));
		// then
		PowerMockito.verifyStatic(MimeTypeDetectionFacade.class, times(1));
		MimeTypeDetectionFacade.detectContentType(imageInputStream);

		then(saveFilePort).should(never()).saveFile(any(InputStream.class), any(String.class), any(String.class));
		then(updateUserAccountPort).should(never()).updateIdentityDocument(any(Long.class), any(String.class),
				any(String.class), any(DocumentType.class));

		then(deleteFilePort).should(never()).deleteFile(any(String.class));
	}

	@Test
	@PrepareForTest(MimeTypeDetectionFacade.class)
	public void givenIOExceptionWhenDetectingFileType_whenUploadProfileImage_ThenThrowDocumentAccessException()
			throws IOException {

		// given
		byte[] initialArray = { 0, 1, 2 };
		BufferedInputStream imageInputStream = new BufferedInputStream(new ByteArrayInputStream(initialArray));

		PowerMockito.mockStatic(MimeTypeDetectionFacade.class);
		PowerMockito.when(MimeTypeDetectionFacade.detectContentType(any(InputStream.class)))
				.thenThrow(new IOException("Unable to read from file input stream."));

		// when
		assertThrows(DocumentAccessException.class, () -> identityDocumentService
				.uploadIdentityDocument(imageInputStream, "Image.jpg", TestConstants.DEFAULT_EMAIL));
		// then
		PowerMockito.verifyStatic(MimeTypeDetectionFacade.class, times(1));
		MimeTypeDetectionFacade.detectContentType(imageInputStream);

		then(saveFilePort).should(never()).saveFile(any(InputStream.class), any(String.class), any(String.class));
		then(updateUserAccountPort).should(never()).updateIdentityDocument(any(Long.class), any(String.class),
				any(String.class), any(DocumentType.class));

		then(deleteFilePort).should(never()).deleteFile(any(String.class));
	}

	@Test
	@PrepareForTest(MimeTypeDetectionFacade.class)
	public void givenIOExceptionWhenSavingFile_whenUploadProfileImage_ThenThrowDocumentAccessException()
			throws IOException {

		// given
		byte[] initialArray = { 0, 1, 2 };
		BufferedInputStream imageInputStream = new BufferedInputStream(new ByteArrayInputStream(initialArray));

		doThrow(IOException.class).when(saveFilePort).saveFile(imageInputStream,
				DocumentType.IMAGE_JPEG.getParentFolder(), "Image.jpg");

		PowerMockito.mockStatic(MimeTypeDetectionFacade.class);
		PowerMockito.when(MimeTypeDetectionFacade.detectContentType(any(InputStream.class))).thenReturn("image/jpeg");

		// when // then
		assertThrows(DocumentAccessException.class, () -> identityDocumentService
				.uploadIdentityDocument(imageInputStream, "Image.jpg", TestConstants.DEFAULT_EMAIL));

	}

	@Test
	@PrepareForTest(MimeTypeDetectionFacade.class)
	public void givenIOExceptionWhenDeletingOldFile_whenUploadProfileImage_ThenThrowDocumentAccessException()
			throws IOException {

		// given
		byte[] initialArray = { 0, 1, 2 };
		BufferedInputStream imageInputStream = new BufferedInputStream(new ByteArrayInputStream(initialArray));

		Long currentTime = new Date().getTime();
		given(saveFilePort.saveFile(imageInputStream, DocumentType.IMAGE_JPEG.getParentFolder(), "Image.jpg"))
				.willReturn("/Images/" + currentTime + "-Image.jpg");

		UserAccount userAccount = UserAccountTestData.defaultUserAccountBuilder().build();
		given(loadUserAccountPort.loadUserAccountByUsername(TestConstants.DEFAULT_EMAIL))
				.willReturn(Optional.of(userAccount));

		given(loadUserAccountPort.hasNoIdentityImage(userAccount.getId())).willReturn(false);
		given(loadUserAccountPort.loadIdentityDocumentLocation(userAccount.getId()))
				.willReturn("/Images/default_identity.jpg");

		PowerMockito.mockStatic(MimeTypeDetectionFacade.class);
		PowerMockito.when(MimeTypeDetectionFacade.detectContentType(any(InputStream.class))).thenReturn("image/jpeg");

		doThrow(IOException.class).when(deleteFilePort).deleteFile("/Images/default_identity.jpg");

		// when //then
		assertThrows(DocumentAccessException.class, () -> identityDocumentService
				.uploadIdentityDocument(imageInputStream, "Image.jpg", TestConstants.DEFAULT_EMAIL));

	}

}
