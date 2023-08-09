package com.excentria_it.wamya.application.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.MediaType;

import com.excentria_it.wamya.application.port.in.UploadProfileImageUseCase;
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

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class ProfileImageService implements UploadProfileImageUseCase {

	private final SaveFilePort saveFilePort;
	private final DeleteFilePort deleteFilePort;

	private final LoadUserAccountPort loadUserAccountPort;
	private final UpdateUserAccountPort updateUserAccountPort;

	@Override
	public void uploadProfileImage(BufferedInputStream imageInputStream, String imageOriginalName, String subject) {

		// detect document type
		String documentType = null;
		try {
			documentType = MimeTypeDetectionFacade.detectContentType(imageInputStream);
		} catch (IOException e) {
			throw new DocumentAccessException(e.getMessage());
		}

		// check document type is jpeg image
		if (!MediaType.IMAGE_JPEG_VALUE.equals(documentType.toLowerCase())) {
			throw new UnsupportedMimeTypeException(
					String.format("Unsupported mime type: %s. Supported mime types are: %s", documentType.toLowerCase(),
							MediaType.IMAGE_JPEG_VALUE));
		}

		// save image to underlying storage system

		String location;
		try {
			location = saveFilePort.saveFile(imageInputStream, DocumentType.IMAGE_JPEG.getParentFolder(),
					imageOriginalName);
		} catch (IOException e) {
			throw new DocumentAccessException(e.getMessage());
		}
		String hash = DigestUtils.sha256Hex(location.getBytes());

		// create database document entry for image file
		Optional<UserAccount> userAccountOptional = loadUserAccountPort.loadUserAccountBySubject(subject);
		Long userId = userAccountOptional.get().getId();

		String oldAvatarFileLocation = null;
		Boolean shouldDeleteOldImage = !loadUserAccountPort.hasDefaultProfileImage(userId);
		if (shouldDeleteOldImage) {

			oldAvatarFileLocation = loadUserAccountPort.loadProfileImageLocation(userId);
		}
		// update user profile image Document
		updateUserAccountPort.updateUserProfileImage(userId, location, hash);

		// delete old profile image physical file if not default
		if (shouldDeleteOldImage) {

			try {
				deleteFilePort.deleteFile(oldAvatarFileLocation);
			} catch (IOException e) {
				throw new DocumentAccessException(e.getMessage());
			}
		}
	}

}
