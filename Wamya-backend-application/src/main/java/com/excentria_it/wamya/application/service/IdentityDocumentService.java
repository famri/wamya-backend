package com.excentria_it.wamya.application.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.MediaType;

import com.excentria_it.wamya.application.port.in.UploadIdentityDocumentUseCase;
import com.excentria_it.wamya.application.port.out.DeleteFilePort;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.SaveFilePort;
import com.excentria_it.wamya.application.port.out.UpdateUserAccountPort;
import com.excentria_it.wamya.application.utils.MimeTypeDetectionFacade;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.exception.DocumentAccessException;
import com.excentria_it.wamya.common.exception.UnsupportedMimeTypeException;
import com.excentria_it.wamya.domain.DocumentType;
import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.domain.ValidationState;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class IdentityDocumentService implements UploadIdentityDocumentUseCase {
	private final SaveFilePort saveFilePort;
	private final DeleteFilePort deleteFilePort;

	private final LoadUserAccountPort loadUserAccountPort;
	private final UpdateUserAccountPort updateUserAccountPort;

	@Override
	public void uploadIdentityDocument(BufferedInputStream documentInputStream, String originalFilename,
			String subject) {
		// detect document type
		String mediaType = null;
		try {
			mediaType = MimeTypeDetectionFacade.detectContentType(documentInputStream);
		} catch (IOException e) {
			throw new DocumentAccessException(e.getMessage());
		}

		// check document type is jpeg image
		if (!MediaType.IMAGE_JPEG_VALUE.equals(mediaType.toLowerCase())
				&& !MediaType.APPLICATION_PDF_VALUE.equals(mediaType.toLowerCase())) {
			throw new UnsupportedMimeTypeException(
					String.format("Unsupported mime type: %s. Supported mime types are: %s", mediaType.toLowerCase(),
							MediaType.IMAGE_JPEG_VALUE + ", " + MediaType.APPLICATION_PDF_VALUE));
		}

		// save document to underlying storage system
		DocumentType documentType;
		if (MediaType.IMAGE_JPEG_VALUE.equals(mediaType.toLowerCase())) {
			documentType = DocumentType.IMAGE_JPEG;
		} else {
			documentType = DocumentType.APPLICATION_PDF;
		}

		String location;
		try {

			location = saveFilePort.saveFile(documentInputStream, documentType.getParentFolder(), originalFilename);
		} catch (IOException e) {
			throw new DocumentAccessException(e.getMessage());
		}
		String hash = DigestUtils.sha256Hex(location.getBytes());

		// create database document entry for document file
		Optional<UserAccount> userAccountOptional = loadUserAccountPort.loadUserAccountBySubject(subject);
		Long userId = userAccountOptional.get().getId();

		String oldIdentityFileLocation = null;
		Boolean shouldDeleteOldIdentity = !loadUserAccountPort.hasNoIdentityImage(userId);
		if (shouldDeleteOldIdentity) {

			oldIdentityFileLocation = loadUserAccountPort.loadIdentityDocumentLocation(userId);
		}
		// update user profile image Document
		updateUserAccountPort.updateIdentityDocument(userId, location, hash, documentType);
		
		// change identity validation state to PENDING
		updateUserAccountPort.updateIdentityValidationState(userId, ValidationState.PENDING);
		
		// delete old profile image physical file if not default
		if (shouldDeleteOldIdentity) {

			try {
				deleteFilePort.deleteFile(oldIdentityFileLocation);
			} catch (IOException e) {
				throw new DocumentAccessException(e.getMessage());
			}
		}

	}

}
