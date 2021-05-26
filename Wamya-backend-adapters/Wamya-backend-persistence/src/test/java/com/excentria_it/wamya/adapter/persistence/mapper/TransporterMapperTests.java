package com.excentria_it.wamya.adapter.persistence.mapper;

import static com.excentria_it.wamya.test.data.common.DocumentJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.GenderJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.InternationalCallingCodeJpaEntityTestData.*;
import static com.excentria_it.wamya.test.data.common.UserAccountTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.adapter.persistence.entity.DocumentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GenderJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.domain.UserAccount;

public class TransporterMapperTests {

	private TransporterMapper transporterMapper = new TransporterMapper();

	@Test
	void testMapToJpaEntity() {
		UserAccount userAccount = defaultUserAccountBuilder().isTransporter(true).build();
		InternationalCallingCodeJpaEntity icc = defaultExistentInternationalCallingCodeJpaEntity();
		GenderJpaEntity gender = manGenderJpaEntity();
		DocumentJpaEntity profileImage = defaultManProfileImageDocumentJpaEntity();
		TransporterJpaEntity transporterJpaEntity = transporterMapper.mapToJpaEntity(userAccount, icc, gender,
				profileImage);

		assertEquals(userAccount.getId(), transporterJpaEntity.getId());
		assertEquals(userAccount.getOauthId(), transporterJpaEntity.getOauthId());
		assertEquals(userAccount.getIsTransporter(), true);
		assertEquals(gender, transporterJpaEntity.getGender());
		assertEquals(userAccount.getFirstname(), transporterJpaEntity.getFirstname());
		assertEquals(userAccount.getLastname(), transporterJpaEntity.getLastname());
		assertEquals(userAccount.getDateOfBirth(), transporterJpaEntity.getDateOfBirth());
		assertEquals(userAccount.getEmail(), transporterJpaEntity.getEmail());
		assertEquals(userAccount.getEmailValidationCode(), transporterJpaEntity.getEmailValidationCode());
		assertEquals(userAccount.getIsValidatedEmail(), transporterJpaEntity.getIsValidatedEmail());
		assertEquals(
				userAccount.getMobilePhoneNumber().getInternationalCallingCode() + "_"
						+ userAccount.getMobilePhoneNumber().getMobileNumber(),
				transporterJpaEntity.getIcc().getValue() + "_" + transporterJpaEntity.getMobileNumber());
		assertEquals(userAccount.getMobileNumberValidationCode(), transporterJpaEntity.getMobileNumberValidationCode());

		assertEquals(userAccount.getIsValidatedMobileNumber(), transporterJpaEntity.getIsValidatedMobileNumber());

		assertEquals(userAccount.getReceiveNewsletter(), transporterJpaEntity.getReceiveNewsletter());
		assertTrue(userAccount.getCreationDateTime()
				.isEqual(transporterJpaEntity.getCreationDateTime().atZone(ZoneOffset.UTC)));
		assertEquals(profileImage, transporterJpaEntity.getProfileImage());
	}

	@Test
	void testMapNullToJpaEntity() {
		InternationalCallingCodeJpaEntity icc = defaultExistentInternationalCallingCodeJpaEntity();
		GenderJpaEntity gender = manGenderJpaEntity();
		DocumentJpaEntity profileImage = defaultManProfileImageDocumentJpaEntity();
		TransporterJpaEntity transporterJpaEntity = transporterMapper.mapToJpaEntity(null, icc, gender, profileImage);
		assertNull(transporterJpaEntity);
	}

	@Test
	void testMapNotTransporterToJpaEntity() {
		UserAccount userAccount = defaultUserAccountBuilder().isTransporter(false).build();
		InternationalCallingCodeJpaEntity icc = defaultExistentInternationalCallingCodeJpaEntity();
		GenderJpaEntity gender = manGenderJpaEntity();
		DocumentJpaEntity profileImage = defaultManProfileImageDocumentJpaEntity();
		TransporterJpaEntity transporterJpaEntity = transporterMapper.mapToJpaEntity(userAccount, icc, gender,
				profileImage);
		assertNull(transporterJpaEntity);
	}
}
