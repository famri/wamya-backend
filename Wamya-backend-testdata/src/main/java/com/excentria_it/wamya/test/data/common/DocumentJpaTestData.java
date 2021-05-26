package com.excentria_it.wamya.test.data.common;

import java.time.Instant;

import org.apache.commons.codec.digest.DigestUtils;

import com.excentria_it.wamya.adapter.persistence.entity.DocumentJpaEntity;
import com.excentria_it.wamya.domain.DocumentType;

public class DocumentJpaTestData {
	public static DocumentJpaEntity defaultManProfileImageDocumentJpaEntity() {
		DocumentJpaEntity document = new DocumentJpaEntity(null, "/Images/1621329097610-default_man_avatar.jpg",
				DocumentType.IMAGE_JPEG, Instant.ofEpochMilli(1621329097610L), // null,
				DigestUtils.sha256Hex("/Images/1621329097610-default_man_avatar.jpg".getBytes()), true);
		document.setId(-1L);

		return document;

	}

	public static DocumentJpaEntity defaultWomanProfileImageDocumentJpaEntity() {
		DocumentJpaEntity document = new DocumentJpaEntity(null, "/Images/1621329097610-default_woman_avatar.jpg",
				DocumentType.IMAGE_JPEG, Instant.ofEpochMilli(1621329097610L), // null,
				DigestUtils.sha256Hex("/Images/1621329097610-default_woman_avatar.jpg".getBytes()), true);
		document.setId(-2L);
		return document;
	}

	public static DocumentJpaEntity nonDefaultManProfileImageDocumentJpaEntity() {
		DocumentJpaEntity document = new DocumentJpaEntity(null, "/Images/1621329097610-some_man_avatar.jpg",
				DocumentType.IMAGE_JPEG, Instant.ofEpochMilli(1621329097610L),
				DigestUtils.sha256Hex("/Images/1621329097610-some_man_avatar.jpg".getBytes()), false);
		document.setId(1L);

		return document;

	}

	public static DocumentJpaEntity nonDefaultWomanProfileImageDocumentJpaEntity() {
		DocumentJpaEntity document = new DocumentJpaEntity(null, "/Images/1621329097610-some_woman_avatar.jpg",
				DocumentType.IMAGE_JPEG, Instant.ofEpochMilli(1621329097610L), // null,
				DigestUtils.sha256Hex("/Images/1621329097610-some_woman_avatar.jpg".getBytes()), false);
		document.setId(2L);

		return document;

	}

	public static DocumentJpaEntity defaultVehiculeImageDocumentJpaEntity() {
		DocumentJpaEntity document = new DocumentJpaEntity(null, "/Images/1621329097610-default_vehicule_avatar.jpg",
				DocumentType.IMAGE_JPEG, Instant.ofEpochMilli(1621329097610L), // null,
				DigestUtils.sha256Hex("/Images/1621329097610-default_vehicule_avatar.jpg".getBytes()), true);
		document.setId(-3L);
		return document;
	}
}
