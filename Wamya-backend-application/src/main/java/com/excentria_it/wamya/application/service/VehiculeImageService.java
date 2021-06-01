package com.excentria_it.wamya.application.service;

import java.io.BufferedInputStream;
import java.io.IOException;

import javax.transaction.Transactional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.MediaType;

import com.excentria_it.wamya.application.port.in.UploadVehiculeImageUseCase;
import com.excentria_it.wamya.application.port.out.CheckUserVehiculePort;
import com.excentria_it.wamya.application.port.out.DeleteFilePort;
import com.excentria_it.wamya.application.port.out.LoadVehiculePort;
import com.excentria_it.wamya.application.port.out.SaveFilePort;
import com.excentria_it.wamya.application.port.out.UpdateVehiculePort;
import com.excentria_it.wamya.application.utils.MimeTypeDetectionFacade;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.exception.DocumentAccessException;
import com.excentria_it.wamya.common.exception.ForbiddenAccessException;
import com.excentria_it.wamya.common.exception.UnsupportedMimeTypeException;
import com.excentria_it.wamya.domain.DocumentType;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class VehiculeImageService implements UploadVehiculeImageUseCase {

	private final SaveFilePort saveFilePort;
	private final DeleteFilePort deleteFilePort;
	private final CheckUserVehiculePort checkUserVehiculePort;
	private final UpdateVehiculePort updateVehiculePort;
	private final LoadVehiculePort loadVehiculePort;

	@Override
	public void uploadVehiculeImage(BufferedInputStream imageInputStream, String imageOriginalName, Long vehiculeId,
			String username) {

		Boolean isUserVehicule = checkUserVehiculePort.isUserVehicule(username, vehiculeId);

		if (!isUserVehicule) {
			throw new ForbiddenAccessException("You are not authorized to change vehicule image.");
		}

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
		String oldVehiculeImageFileLocation = null;
		Boolean shouldDeleteOldImage = !loadVehiculePort.hasDefaultVehiculeImage(vehiculeId);
		if (shouldDeleteOldImage) {

			oldVehiculeImageFileLocation = loadVehiculePort.loadImageLocation(vehiculeId);
		}
		// update user profile image Document
		updateVehiculePort.updateVehiculeImage(username, vehiculeId, location, hash);

		// delete old profile image physical file if not default
		if (shouldDeleteOldImage) {

			try {
				deleteFilePort.deleteFile(oldVehiculeImageFileLocation);
			} catch (IOException e) {
				throw new DocumentAccessException(e.getMessage());
			}
		}

	}

}
