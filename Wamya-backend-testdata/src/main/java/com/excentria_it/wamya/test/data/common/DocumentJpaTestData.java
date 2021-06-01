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
	public static DocumentJpaEntity jpegIdentityDocumentJpaEntity() {
		DocumentJpaEntity document = new DocumentJpaEntity(null, "/Images/1621329097610-default_identity.jpg",
				DocumentType.IMAGE_JPEG, Instant.ofEpochMilli(1621329097610L), null,
				DigestUtils.sha256Hex("/Images/1621329097610-default_identity.jpg".getBytes()), true);
		document.setId(-5L);

		return document;

	}

	public static DocumentJpaEntity pdfIdentityDocumentJpaEntity() {
		DocumentJpaEntity document = new DocumentJpaEntity(null, "/Images/1621329097610-default_identity.pdf",
				DocumentType.APPLICATION_PDF, Instant.ofEpochMilli(1621329097610L), null,
				DigestUtils.sha256Hex("/Images/1621329097610-default_identity.pdf".getBytes()), true);
		document.setId(-6L);

		return document;

	}

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

	public static DocumentJpaEntity nonDefaultVehiculeImageDocumentJpaEntity() {
		EntitlementJpaEntity ownerEntitlement = new EntitlementJpaEntity(EntitlementType.OWNER, true, true);
		EntitlementJpaEntity othersEntitlement = new EntitlementJpaEntity(EntitlementType.OTHERS, true, false);
		EntitlementJpaEntity supportEntitlement = new EntitlementJpaEntity(EntitlementType.SUPPORT, true, true);
		DocumentJpaEntity document = new DocumentJpaEntity(null,
				"/Images/1621329097610-non_default_vehicule_avatar.jpg", DocumentType.IMAGE_JPEG,
				Instant.ofEpochMilli(1621329097610L), Set.of(ownerEntitlement, othersEntitlement, supportEntitlement),
				DigestUtils.sha256Hex("/Images/1621329097610-non_default_vehicule_avatar.jpg".getBytes()), false);
		document.setId(-4L);
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

	public static DocumentJpaEntity defaultUtilityVehiculeImageDocumentJpaEntity() {
		DocumentJpaEntity document = new DocumentJpaEntity(null,
				"/Images/1621329097610-default_utility_vehicule_image.jpg", DocumentType.IMAGE_JPEG,
				Instant.ofEpochMilli(1621329097610L), null,
				DigestUtils.sha256Hex("/Images/1621329097610-default_utility_vehicule_image.jpg".getBytes()), true);
		document.setId(-11L);

		return document;

	}

	public static DocumentJpaEntity defaultPickupVehiculeImageDocumentJpaEntity() {
		DocumentJpaEntity document = new DocumentJpaEntity(null,
				"/Images/1621329097610-default_pickup_vehicule_image.jpg", DocumentType.IMAGE_JPEG,
				Instant.ofEpochMilli(1621329097610L), null,
				DigestUtils.sha256Hex("/Images/1621329097610-default_pickup_vehicule_image.jpg".getBytes()), true);
		document.setId(-12L);

		return document;

	}

	public static DocumentJpaEntity defaultBusVehiculeImageDocumentJpaEntity() {
		DocumentJpaEntity document = new DocumentJpaEntity(null, "/Images/1621329097610-default_bus_vehicule_image.jpg",
				DocumentType.IMAGE_JPEG, Instant.ofEpochMilli(1621329097610L), null,
				DigestUtils.sha256Hex("/Images/1621329097610-default_bus_vehicule_image.jpg".getBytes()), true);
		document.setId(-13L);

		return document;

	}

	public static DocumentJpaEntity defaultMinibusVehiculeImageDocumentJpaEntity() {
		DocumentJpaEntity document = new DocumentJpaEntity(null,
				"/Images/1621329097610-default_minibus_vehicule_image.jpg", DocumentType.IMAGE_JPEG,
				Instant.ofEpochMilli(1621329097610L), null,
				DigestUtils.sha256Hex("/Images/1621329097610-default_minibus_vehicule_image.jpg".getBytes()), true);
		document.setId(-14L);

		return document;

	}

	public static DocumentJpaEntity defaultVanL1H1VehiculeImageDocumentJpaEntity() {
		DocumentJpaEntity document = new DocumentJpaEntity(null,
				"/Images/1621329097610-default_vanL1H1_vehicule_image.jpg", DocumentType.IMAGE_JPEG,
				Instant.ofEpochMilli(1621329097610L), null,
				DigestUtils.sha256Hex("/Images/1621329097610-default_vanL1H1_vehicule_image.jpg".getBytes()), true);
		document.setId(-15L);

		return document;

	}

	public static DocumentJpaEntity defaultVanL2H2VehiculeImageDocumentJpaEntity() {
		DocumentJpaEntity document = new DocumentJpaEntity(null,
				"/Images/1621329097610-default_vanL2H2_vehicule_image.jpg", DocumentType.IMAGE_JPEG,
				Instant.ofEpochMilli(1621329097610L), null,
				DigestUtils.sha256Hex("/Images/1621329097610-default_vanL2H2_vehicule_image.jpg".getBytes()), true);
		document.setId(-16L);

		return document;

	}

	public static DocumentJpaEntity defaultVanL3H3VehiculeImageDocumentJpaEntity() {
		DocumentJpaEntity document = new DocumentJpaEntity(null,
				"/Images/1621329097610-default_vanL3H3_vehicule_image.jpg", DocumentType.IMAGE_JPEG,
				Instant.ofEpochMilli(1621329097610L), null,
				DigestUtils.sha256Hex("/Images/1621329097610-default_vanL3H3_vehicule_image.jpg".getBytes()), true);
		document.setId(-15L);

		return document;

	}

	public static DocumentJpaEntity defaultVanL4H3VehiculeImageDocumentJpaEntity() {
		DocumentJpaEntity document = new DocumentJpaEntity(null,
				"/Images/1621329097610-default_vanL4H3_vehicule_image.jpg", DocumentType.IMAGE_JPEG,
				Instant.ofEpochMilli(1621329097610L), null,
				DigestUtils.sha256Hex("/Images/1621329097610-default_vanL4H3_vehicule_image.jpg".getBytes()), true);
		document.setId(-17L);

		return document;

	}

	public static DocumentJpaEntity defaultFlatbedTruckVehiculeImageDocumentJpaEntity() {
		DocumentJpaEntity document = new DocumentJpaEntity(null,
				"/Images/1621329097610-default_flatbed_truck_vehicule_image.jpg", DocumentType.IMAGE_JPEG,
				Instant.ofEpochMilli(1621329097610L), null,
				DigestUtils.sha256Hex("/Images/1621329097610-default_flatbed_truck_vehicule_image.jpg".getBytes()),
				true);
		document.setId(-18L);

		return document;

	}

	public static DocumentJpaEntity defaultBoxTruckVehiculeImageDocumentJpaEntity() {
		DocumentJpaEntity document = new DocumentJpaEntity(null,
				"/Images/1621329097610-default_box_truck_vehicule_image.jpg", DocumentType.IMAGE_JPEG,
				Instant.ofEpochMilli(1621329097610L), null,
				DigestUtils.sha256Hex("/Images/1621329097610-default_box_truck_vehicule_image.jpg".getBytes()), true);
		document.setId(-19L);

		return document;

	}

	public static DocumentJpaEntity defaultRegrigeratedTruckVehiculeImageDocumentJpaEntity() {
		DocumentJpaEntity document = new DocumentJpaEntity(null,
				"/Images/1621329097610-default_refrigerated_truck_vehicule_image.jpg", DocumentType.IMAGE_JPEG,
				Instant.ofEpochMilli(1621329097610L), null,
				DigestUtils.sha256Hex("/Images/1621329097610-default_refrigerated_truck_vehicule_image.jpg".getBytes()),
				true);
		document.setId(-20L);

		return document;

	}

	public static DocumentJpaEntity defaultTankerVehiculeImageDocumentJpaEntity() {
		DocumentJpaEntity document = new DocumentJpaEntity(null,
				"/Images/1621329097610-default_tanker_vehicule_image.jpg", DocumentType.IMAGE_JPEG,
				Instant.ofEpochMilli(1621329097610L), null,
				DigestUtils.sha256Hex("/Images/1621329097610-default_tanker_vehicule_image.jpg".getBytes()), true);
		document.setId(-21L);

		return document;

	}

	public static DocumentJpaEntity defaultDumpTruckVehiculeImageDocumentJpaEntity() {
		DocumentJpaEntity document = new DocumentJpaEntity(null,
				"/Images/1621329097610-default_dump_truck_vehicule_image.jpg", DocumentType.IMAGE_JPEG,
				Instant.ofEpochMilli(1621329097610L), null,
				DigestUtils.sha256Hex("/Images/1621329097610-default_dump_truck_vehicule_image.jpg".getBytes()), true);
		document.setId(-22L);

		return document;

	}

	public static DocumentJpaEntity defaultHookLiftTruckVehiculeImageDocumentJpaEntity() {
		DocumentJpaEntity document = new DocumentJpaEntity(null,
				"/Images/1621329097610-default_hook_lift_truck_vehicule_image.jpg", DocumentType.IMAGE_JPEG,
				Instant.ofEpochMilli(1621329097610L), null,
				DigestUtils.sha256Hex("/Images/1621329097610-default_hook_lift_truck_vehicule_image.jpg".getBytes()),
				true);
		document.setId(-23L);

		return document;

	}

	public static DocumentJpaEntity defaultTankTransporterVehiculeImageDocumentJpaEntity() {
		DocumentJpaEntity document = new DocumentJpaEntity(null,
				"/Images/1621329097610-default_tank_transporter_vehicule_image.jpg", DocumentType.IMAGE_JPEG,
				Instant.ofEpochMilli(1621329097610L), null,
				DigestUtils.sha256Hex("/Images/1621329097610-default_tank_transporter_vehicule_image.jpg".getBytes()),
				true);
		document.setId(-24L);

		return document;

	}

}
