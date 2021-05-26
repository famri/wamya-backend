package com.excentria_it.wamya.domain;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum DocumentType {

	IMAGE_JPEG("image/jpeg", "Images"), APPLICATION_PDF("application/pdf", "Pdfs");

	private String mediaType;
	private String parentFolder;

	DocumentType(String mediaType, String parentFolder) {
		this.mediaType = mediaType;
		this.parentFolder = parentFolder;
	}

	static public DocumentType from(String mediaType) {

		return Arrays.asList(DocumentType.values()).stream().filter(dt -> dt.mediaType == mediaType).findFirst()
				.orElse(null);
	}

}
