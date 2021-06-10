package com.excentria_it.wamya.adapter.persistence.mapper;

import static com.excentria_it.wamya.test.data.common.DocumentJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.GenderJpaTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.ZoneOffset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DocumentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GenderJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.application.utils.DocumentUrlResolver;
import com.excentria_it.wamya.domain.ProfileInfoDto;
import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.test.data.common.InternationalCallingCodeJpaEntityTestData;
import com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData;
import com.excentria_it.wamya.test.data.common.UserAccountTestData;

public class UserAccountMapperTests {
	private DocumentUrlResolver documentUrlResolver = new DocumentUrlResolver();
	private UserAccountMapper userAccountMapper = new UserAccountMapper(documentUrlResolver);

	@BeforeEach
	void initDocumentUrlResolver() {
		documentUrlResolver.setServerBaseUrl("https://domain-name:port/wamya-backend");
	}

	@Test
	void testMapClientUserAccountToJpaEntity() {

		UserAccount userAccount = UserAccountTestData.defaultUserAccountBuilder().isTransporter(false).build();
		InternationalCallingCodeJpaEntity iccEntity = InternationalCallingCodeJpaEntityTestData
				.defaultExistentInternationalCallingCodeJpaEntity();
		GenderJpaEntity gender = manGenderJpaEntity();
		DocumentJpaEntity profileImage = defaultManProfileImageDocumentJpaEntity();
		DocumentJpaEntity identityDocument = jpegIdentityDocumentJpaEntity();
		UserAccountJpaEntity userAccountJpaEntity = userAccountMapper.mapToJpaEntity(userAccount, iccEntity, gender,
				profileImage, identityDocument);

		assertEquals(userAccount.getId(), userAccountJpaEntity.getId());

		assertTrue(userAccountJpaEntity instanceof ClientJpaEntity);

		assertEquals(gender, userAccountJpaEntity.getGender());

		assertEquals(userAccount.getFirstname(), userAccountJpaEntity.getFirstname());

		assertEquals(userAccount.getLastname(), userAccountJpaEntity.getLastname());

		assertEquals(userAccount.getDateOfBirth(), userAccountJpaEntity.getDateOfBirth());

		assertEquals(userAccount.getEmail(), userAccountJpaEntity.getEmail());

		assertEquals(userAccount.getEmailValidationCode(), userAccountJpaEntity.getEmailValidationCode());

		assertEquals(userAccount.getIsValidatedEmail(), userAccountJpaEntity.getIsValidatedEmail());

		assertEquals(iccEntity.getId(), userAccountJpaEntity.getIcc().getId());

		assertEquals(iccEntity.getValue(), userAccountJpaEntity.getIcc().getValue());

		assertEquals(userAccount.getMobilePhoneNumber().getMobileNumber(), userAccountJpaEntity.getMobileNumber());

		assertEquals(userAccount.getMobileNumberValidationCode(), userAccountJpaEntity.getMobileNumberValidationCode());

		assertEquals(userAccount.getIsValidatedMobileNumber(), userAccountJpaEntity.getIsValidatedMobileNumber());

		assertEquals(userAccount.getReceiveNewsletter(), userAccountJpaEntity.getReceiveNewsletter());

		assertTrue(userAccount.getCreationDateTime()
				.isEqual(userAccountJpaEntity.getCreationDateTime().atZone(ZoneOffset.UTC)));

	}

	@Test
	void testMapTransporterUserAccountToJpaEntity() {

		UserAccount userAccount = UserAccountTestData.defaultUserAccountBuilder().isTransporter(true).build();
		InternationalCallingCodeJpaEntity iccEntity = InternationalCallingCodeJpaEntityTestData
				.defaultExistentInternationalCallingCodeJpaEntity();
		GenderJpaEntity gender = manGenderJpaEntity();
		DocumentJpaEntity profileImage = defaultManProfileImageDocumentJpaEntity();
		DocumentJpaEntity identityDocument = jpegIdentityDocumentJpaEntity();
		UserAccountJpaEntity userAccountJpaEntity = userAccountMapper.mapToJpaEntity(userAccount, iccEntity, gender,
				profileImage, identityDocument);

		assertEquals(userAccount.getId(), userAccountJpaEntity.getId());

		assertEquals(userAccount.getOauthId(), userAccountJpaEntity.getOauthId());

		assertTrue(userAccountJpaEntity instanceof TransporterJpaEntity);

		assertEquals(gender, userAccountJpaEntity.getGender());

		assertEquals(userAccount.getFirstname(), userAccountJpaEntity.getFirstname());

		assertEquals(userAccount.getLastname(), userAccountJpaEntity.getLastname());

		assertEquals(userAccount.getDateOfBirth(), userAccountJpaEntity.getDateOfBirth());

		assertEquals(userAccount.getEmail(), userAccountJpaEntity.getEmail());

		assertEquals(userAccount.getEmailValidationCode(), userAccountJpaEntity.getEmailValidationCode());

		assertEquals(userAccount.getIsValidatedEmail(), userAccountJpaEntity.getIsValidatedEmail());

		assertEquals(iccEntity.getId(), userAccountJpaEntity.getIcc().getId());

		assertEquals(iccEntity.getValue(), userAccountJpaEntity.getIcc().getValue());

		assertEquals(userAccount.getMobilePhoneNumber().getMobileNumber(), userAccountJpaEntity.getMobileNumber());

		assertEquals(userAccount.getMobileNumberValidationCode(), userAccountJpaEntity.getMobileNumberValidationCode());

		assertEquals(userAccount.getIsValidatedMobileNumber(), userAccountJpaEntity.getIsValidatedMobileNumber());

		assertEquals(userAccount.getReceiveNewsletter(), userAccountJpaEntity.getReceiveNewsletter());

		assertTrue(userAccount.getCreationDateTime()
				.isEqual(userAccountJpaEntity.getCreationDateTime().atZone(ZoneOffset.UTC)));

	}

	@Test
	void testMapToJpaEntityWithNullClientAccountCreationDateTime() {

		UserAccount userAccount = UserAccountTestData.defaultUserAccountBuilder().creationDateTime(null).build();
		InternationalCallingCodeJpaEntity iccEntity = InternationalCallingCodeJpaEntityTestData
				.defaultExistentInternationalCallingCodeJpaEntity();
		GenderJpaEntity gender = manGenderJpaEntity();
		DocumentJpaEntity profileImage = defaultManProfileImageDocumentJpaEntity();
		DocumentJpaEntity identityDocument = jpegIdentityDocumentJpaEntity();
		UserAccountJpaEntity userAccountJpaEntity = userAccountMapper.mapToJpaEntity(userAccount, iccEntity, gender,
				profileImage, identityDocument);

		assertNotNull(userAccountJpaEntity.getCreationDateTime());

	}

	@Test
	void testMapToJpaEntityWithNullTransporterAccountCreationDateTime() {

		UserAccount userAccount = UserAccountTestData.defaultUserAccountBuilder().isTransporter(true)
				.creationDateTime(null).build();
		InternationalCallingCodeJpaEntity iccEntity = InternationalCallingCodeJpaEntityTestData
				.defaultExistentInternationalCallingCodeJpaEntity();
		GenderJpaEntity gender = manGenderJpaEntity();
		DocumentJpaEntity profileImage = defaultManProfileImageDocumentJpaEntity();
		DocumentJpaEntity identityDocument = jpegIdentityDocumentJpaEntity();
		UserAccountJpaEntity userAccountJpaEntity = userAccountMapper.mapToJpaEntity(userAccount, iccEntity, gender,
				profileImage, identityDocument);

		assertNotNull(userAccountJpaEntity.getCreationDateTime());

	}

	@Test
	void testMapToJpaEntityWithNullUserAccount() {

		UserAccount userAccount = null;
		InternationalCallingCodeJpaEntity iccEntity = InternationalCallingCodeJpaEntityTestData
				.defaultExistentInternationalCallingCodeJpaEntity();
		GenderJpaEntity gender = manGenderJpaEntity();
		DocumentJpaEntity profileImage = defaultManProfileImageDocumentJpaEntity();
		DocumentJpaEntity identityDocument = jpegIdentityDocumentJpaEntity();
		UserAccountJpaEntity userAccountJpaEntity = userAccountMapper.mapToJpaEntity(userAccount, iccEntity, gender,
				profileImage, identityDocument);

		assertNull(userAccountJpaEntity);

	}

	@Test
	void testMapToDomainEntity() {

		UserAccountJpaEntity userAccountJpaEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();

		UserAccount userAccount = userAccountMapper.mapToDomainEntity(userAccountJpaEntity);

		assertEquals(userAccount.getId(), userAccountJpaEntity.getId());

		assertEquals(userAccount.getIsTransporter(), userAccountJpaEntity instanceof TransporterJpaEntity);

		assertEquals(userAccount.getGenderId(), userAccountJpaEntity.getGender().getId());

		assertEquals(userAccount.getFirstname(), userAccountJpaEntity.getFirstname());

		assertEquals(userAccount.getLastname(), userAccountJpaEntity.getLastname());

		assertEquals(userAccount.getDateOfBirth(), userAccountJpaEntity.getDateOfBirth());

		assertEquals(userAccount.getEmail(), userAccountJpaEntity.getEmail());

		assertEquals(userAccount.getEmailValidationCode(), userAccountJpaEntity.getEmailValidationCode());

		assertEquals(userAccount.getIsValidatedEmail(), userAccountJpaEntity.getIsValidatedEmail());

		assertEquals(userAccount.getMobilePhoneNumber().getInternationalCallingCode(),
				userAccountJpaEntity.getIcc().getValue());

		assertEquals(userAccount.getMobilePhoneNumber().getMobileNumber(), userAccountJpaEntity.getMobileNumber());

		assertEquals(userAccount.getMobileNumberValidationCode(), userAccountJpaEntity.getMobileNumberValidationCode());

		assertEquals(userAccount.getIsValidatedMobileNumber(), userAccountJpaEntity.getIsValidatedMobileNumber());

		assertEquals(userAccount.getReceiveNewsletter(), userAccountJpaEntity.getReceiveNewsletter());

		assertTrue(userAccount.getCreationDateTime()
				.isEqual(userAccountJpaEntity.getCreationDateTime().atZone(ZoneOffset.UTC)));

	}

	@Test
	void testMapToDomainEntityFromNullUserAccountJpaEntity() {

		UserAccount userAccount = userAccountMapper.mapToDomainEntity(null);

		assertNull(userAccount);

	}

	@Test
	void testMapToProfileInfoDto() {

		// given
		UserAccountJpaEntity userAccountJpaEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();
		// when

		ProfileInfoDto profileInfoDto = userAccountMapper.mapToProfileInfoDto(userAccountJpaEntity, "fr_FR");

		// then
		assertEquals(userAccountJpaEntity.getGender().getId(), profileInfoDto.getGender().getId());
		assertEquals(userAccountJpaEntity.getGender().getName("fr_FR"), profileInfoDto.getGender().getValue());

		assertEquals(userAccountJpaEntity.getFirstname(), profileInfoDto.getName().getFirstname());
		assertEquals(userAccountJpaEntity.getLastname(), profileInfoDto.getName().getLastname());

		assertEquals(userAccountJpaEntity.getIdentityValidationState().isValidated(),
				profileInfoDto.getName().getValidationInfo().getIsValidated());
		assertEquals(userAccountJpaEntity.getIdentityValidationState().name(),
				profileInfoDto.getName().getValidationInfo().getState());

		assertEquals(userAccountJpaEntity.getClass().equals(TransporterJpaEntity.class),
				profileInfoDto.getIsTransporter());

		assertEquals(documentUrlResolver.resolveUrl(userAccountJpaEntity.getProfileImage().getId(),
				userAccountJpaEntity.getProfileImage().getHash()), profileInfoDto.getPhotoUrl());

		assertEquals(userAccountJpaEntity.getDateOfBirth(), profileInfoDto.getDateOfBirth());

		assertEquals(userAccountJpaEntity.getMiniBio(), profileInfoDto.getMiniBio());

		assertEquals(userAccountJpaEntity.getEmail(), profileInfoDto.getEmail().getValue());
		assertEquals(userAccountJpaEntity.getIsValidatedEmail(), profileInfoDto.getEmail().getChecked());

		assertEquals(userAccountJpaEntity.getMobileNumber(), profileInfoDto.getMobile().getValue());

		assertEquals(userAccountJpaEntity.getIcc().getValue(), profileInfoDto.getMobile().getIcc().getValue());

		assertEquals(userAccountJpaEntity.getIcc().getId(), profileInfoDto.getMobile().getIcc().getId());

		assertEquals(userAccountJpaEntity.getIsValidatedMobileNumber(), profileInfoDto.getMobile().getChecked());

	}

}
