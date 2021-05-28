package com.excentria_it.wamya.test.data.common;

import java.time.Instant;
import java.util.Collections;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;

import com.excentria_it.wamya.adapter.persistence.entity.DocumentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.EntitlementJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.domain.DocumentType;
import com.excentria_it.wamya.domain.EntitlementType;

public class DocumentJpaTestData {
	public static DocumentJpaEntity defaultManProfileImageDocumentJpaEntity() {
		DocumentJpaEntity document = new DocumentJpaEntity(null, "/Images/1621329097610-default_man_avatar.jpg",
				DocumentType.IMAGE_JPEG, Instant.ofEpochMilli(1621329097610L), null,
				DigestUtils.sha256Hex("/Images/1621329097610-default_man_avatar.jpg".getBytes()), true);
		document.setId(-1L);

		return document;

	}

	public static DocumentJpaEntity defaultWomanProfileImageDocumentJpaEntity() {
		DocumentJpaEntity document = new DocumentJpaEntity(null, "/Images/1621329097610-default_woman_avatar.jpg",
				DocumentType.IMAGE_JPEG, Instant.ofEpochMilli(1621329097610L), null,
				DigestUtils.sha256Hex("/Images/1621329097610-default_woman_avatar.jpg".getBytes()), true);
		document.setId(-2L);
		return document;
	}

	public static DocumentJpaEntity nonDefaultManProfileImageDocumentJpaEntity() {

		EntitlementJpaEntity ownerEntitlement = new EntitlementJpaEntity(EntitlementType.OWNER, true, true);
		EntitlementJpaEntity othersEntitlement = new EntitlementJpaEntity(EntitlementType.OTHERS, true, false);
		EntitlementJpaEntity supportEntitlement = new EntitlementJpaEntity(EntitlementType.SUPPORT, true, true);

		UserAccountJpaEntity owner = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();

		DocumentJpaEntity document = new DocumentJpaEntity(owner, "/Images/1621329097610-some_man_avatar.jpg",
				DocumentType.IMAGE_JPEG, Instant.ofEpochMilli(1621329097610L),
				Set.of(ownerEntitlement, othersEntitlement, supportEntitlement),
				DigestUtils.sha256Hex("/Images/1621329097610-some_man_avatar.jpg".getBytes()), false);
		document.setId(1L);

		return document;

	}

	public static DocumentJpaEntity nonDefaultManProfileImageWithOwnerReadEntitlementFalseDocumentJpaEntity() {

		EntitlementJpaEntity ownerEntitlement = new EntitlementJpaEntity(EntitlementType.OWNER, false, true);
		EntitlementJpaEntity othersEntitlement = new EntitlementJpaEntity(EntitlementType.OTHERS, true, false);
		EntitlementJpaEntity supportEntitlement = new EntitlementJpaEntity(EntitlementType.SUPPORT, true, true);

		UserAccountJpaEntity owner = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();

		DocumentJpaEntity document = new DocumentJpaEntity(owner, "/Images/1621329097610-some_man_avatar.jpg",
				DocumentType.IMAGE_JPEG, Instant.ofEpochMilli(1621329097610L),
				Set.of(ownerEntitlement, othersEntitlement, supportEntitlement),
				DigestUtils.sha256Hex("/Images/1621329097610-some_man_avatar.jpg".getBytes()), false);
		document.setId(1L);

		return document;

	}

	public static DocumentJpaEntity nonDefaultManProfileImageWithoutOwnerEntitlementDocumentJpaEntity() {

		EntitlementJpaEntity othersEntitlement = new EntitlementJpaEntity(EntitlementType.OTHERS, true, false);
		EntitlementJpaEntity supportEntitlement = new EntitlementJpaEntity(EntitlementType.SUPPORT, true, true);

		UserAccountJpaEntity owner = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();

		DocumentJpaEntity document = new DocumentJpaEntity(owner, "/Images/1621329097610-some_man_avatar.jpg",
				DocumentType.IMAGE_JPEG, Instant.ofEpochMilli(1621329097610L),
				Set.of(othersEntitlement, supportEntitlement),
				DigestUtils.sha256Hex("/Images/1621329097610-some_man_avatar.jpg".getBytes()), false);
		document.setId(1L);

		return document;

	}

	public static DocumentJpaEntity nonDefaultManProfileImageWithNullEntitlementsDocumentJpaEntity() {

		UserAccountJpaEntity owner = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();

		DocumentJpaEntity document = new DocumentJpaEntity(owner, "/Images/1621329097610-some_man_avatar.jpg",
				DocumentType.IMAGE_JPEG, Instant.ofEpochMilli(1621329097610L), null,
				DigestUtils.sha256Hex("/Images/1621329097610-some_man_avatar.jpg".getBytes()), false);
		document.setId(1L);

		return document;

	}

	public static DocumentJpaEntity nonDefaultManProfileImageWithEmptyEntitlementsDocumentJpaEntity() {

		UserAccountJpaEntity owner = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();

		DocumentJpaEntity document = new DocumentJpaEntity(owner, "/Images/1621329097610-some_man_avatar.jpg",
				DocumentType.IMAGE_JPEG, Instant.ofEpochMilli(1621329097610L), Collections.emptySet(),
				DigestUtils.sha256Hex("/Images/1621329097610-some_man_avatar.jpg".getBytes()), false);
		document.setId(1L);

		return document;

	}

	public static DocumentJpaEntity nonDefaultManProfileImageWithoutOwnerDocumentJpaEntity() {

		EntitlementJpaEntity ownerEntitlement = new EntitlementJpaEntity(EntitlementType.OWNER, true, true);
		EntitlementJpaEntity othersEntitlement = new EntitlementJpaEntity(EntitlementType.OTHERS, true, false);
		EntitlementJpaEntity supportEntitlement = new EntitlementJpaEntity(EntitlementType.SUPPORT, true, true);

		DocumentJpaEntity document = new DocumentJpaEntity(null, "/Images/1621329097610-some_man_avatar.jpg",
				DocumentType.IMAGE_JPEG, Instant.ofEpochMilli(1621329097610L),
				Set.of(ownerEntitlement, othersEntitlement, supportEntitlement),
				DigestUtils.sha256Hex("/Images/1621329097610-some_man_avatar.jpg".getBytes()), false);
		document.setId(1L);

		return document;

	}

	public static DocumentJpaEntity nonDefaultWomanProfileImageDocumentJpaEntity() {
		EntitlementJpaEntity ownerEntitlement = new EntitlementJpaEntity(EntitlementType.OWNER, true, true);
		EntitlementJpaEntity othersEntitlement = new EntitlementJpaEntity(EntitlementType.OTHERS, true, false);
		EntitlementJpaEntity supportEntitlement = new EntitlementJpaEntity(EntitlementType.SUPPORT, true, true);

		UserAccountJpaEntity owner = UserAccountJpaEntityTestData.defaultExistentWomanClientJpaEntity();

		DocumentJpaEntity document = new DocumentJpaEntity(owner, "/Images/1621329097610-some_woman_avatar.jpg",
				DocumentType.IMAGE_JPEG, Instant.ofEpochMilli(1621329097610L),
				Set.of(ownerEntitlement, othersEntitlement, supportEntitlement),
				DigestUtils.sha256Hex("/Images/1621329097610-some_woman_avatar.jpg".getBytes()), false);
		document.setId(2L);

		return document;

	}

	public static DocumentJpaEntity defaultVehiculeImageDocumentJpaEntity() {
		EntitlementJpaEntity ownerEntitlement = new EntitlementJpaEntity(EntitlementType.OWNER, true, true);
		EntitlementJpaEntity othersEntitlement = new EntitlementJpaEntity(EntitlementType.OTHERS, true, false);
		EntitlementJpaEntity supportEntitlement = new EntitlementJpaEntity(EntitlementType.SUPPORT, true, true);
		DocumentJpaEntity document = new DocumentJpaEntity(null, "/Images/1621329097610-default_vehicule_avatar.jpg",
				DocumentType.IMAGE_JPEG, Instant.ofEpochMilli(1621329097610L),
				Set.of(ownerEntitlement, othersEntitlement, supportEntitlement),
				DigestUtils.sha256Hex("/Images/1621329097610-default_vehicule_avatar.jpg".getBytes()), true);
		document.setId(-3L);
		return document;
	}

	public static DocumentJpaEntity nonDefaultManProfileImageWithOthersReadEntitlementTrueDocumentJpaEntity() {
		EntitlementJpaEntity ownerEntitlement = new EntitlementJpaEntity(EntitlementType.OWNER, true, true);
		EntitlementJpaEntity othersEntitlement = new EntitlementJpaEntity(EntitlementType.OTHERS, true, false);
		EntitlementJpaEntity supportEntitlement = new EntitlementJpaEntity(EntitlementType.SUPPORT, true, true);

		UserAccountJpaEntity owner = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();

		DocumentJpaEntity document = new DocumentJpaEntity(owner, "/Images/1621329097610-some_man_avatar.jpg",
				DocumentType.IMAGE_JPEG, Instant.ofEpochMilli(1621329097610L),
				Set.of(ownerEntitlement, othersEntitlement, supportEntitlement),
				DigestUtils.sha256Hex("/Images/1621329097610-some_man_avatar.jpg".getBytes()), false);
		document.setId(1L);

		return document;
	}

	public static DocumentJpaEntity nonDefaultManProfileImageWithOthersReadEntitlementFalseDocumentJpaEntity() {
		EntitlementJpaEntity ownerEntitlement = new EntitlementJpaEntity(EntitlementType.OWNER, true, true);
		EntitlementJpaEntity othersEntitlement = new EntitlementJpaEntity(EntitlementType.OTHERS, false, false);
		EntitlementJpaEntity supportEntitlement = new EntitlementJpaEntity(EntitlementType.SUPPORT, true, true);

		UserAccountJpaEntity owner = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();

		DocumentJpaEntity document = new DocumentJpaEntity(owner, "/Images/1621329097610-some_man_avatar.jpg",
				DocumentType.IMAGE_JPEG, Instant.ofEpochMilli(1621329097610L),
				Set.of(ownerEntitlement, othersEntitlement, supportEntitlement),
				DigestUtils.sha256Hex("/Images/1621329097610-some_man_avatar.jpg".getBytes()), false);
		document.setId(1L);

		return document;
	}

	public static DocumentJpaEntity nonDefaultManProfileImageWithNoOthersEntitlementDocumentJpaEntity() {
		EntitlementJpaEntity ownerEntitlement = new EntitlementJpaEntity(EntitlementType.OWNER, true, true);
		EntitlementJpaEntity supportEntitlement = new EntitlementJpaEntity(EntitlementType.SUPPORT, true, true);

		UserAccountJpaEntity owner = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();

		DocumentJpaEntity document = new DocumentJpaEntity(owner, "/Images/1621329097610-some_man_avatar.jpg",
				DocumentType.IMAGE_JPEG, Instant.ofEpochMilli(1621329097610L),
				Set.of(ownerEntitlement, supportEntitlement),
				DigestUtils.sha256Hex("/Images/1621329097610-some_man_avatar.jpg".getBytes()), false);
		document.setId(1L);

		return document;
	}

	public static DocumentJpaEntity nonDefaultManProfileImageWithoutSupportEntitlementDocumentJpaEntity() {
		EntitlementJpaEntity ownerEntitlement = new EntitlementJpaEntity(EntitlementType.OWNER, true, true);
		EntitlementJpaEntity othersEntitlement = new EntitlementJpaEntity(EntitlementType.OTHERS, true, false);

		UserAccountJpaEntity owner = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();

		DocumentJpaEntity document = new DocumentJpaEntity(owner, "/Images/1621329097610-some_man_avatar.jpg",
				DocumentType.IMAGE_JPEG, Instant.ofEpochMilli(1621329097610L),
				Set.of(ownerEntitlement, othersEntitlement),
				DigestUtils.sha256Hex("/Images/1621329097610-some_man_avatar.jpg".getBytes()), false);
		document.setId(1L);

		return document;
	}

	public static DocumentJpaEntity nonDefaultManProfileImageWithSupportEntitlementReadFalseDocumentJpaEntity() {
		EntitlementJpaEntity ownerEntitlement = new EntitlementJpaEntity(EntitlementType.OWNER, true, true);
		EntitlementJpaEntity othersEntitlement = new EntitlementJpaEntity(EntitlementType.OTHERS, true, false);
		EntitlementJpaEntity supportEntitlement = new EntitlementJpaEntity(EntitlementType.SUPPORT, false, false);
		UserAccountJpaEntity owner = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();

		DocumentJpaEntity document = new DocumentJpaEntity(owner, "/Images/1621329097610-some_man_avatar.jpg",
				DocumentType.IMAGE_JPEG, Instant.ofEpochMilli(1621329097610L),
				Set.of(ownerEntitlement, othersEntitlement, supportEntitlement),
				DigestUtils.sha256Hex("/Images/1621329097610-some_man_avatar.jpg".getBytes()), false);
		document.setId(1L);

		return document;
	}
}
