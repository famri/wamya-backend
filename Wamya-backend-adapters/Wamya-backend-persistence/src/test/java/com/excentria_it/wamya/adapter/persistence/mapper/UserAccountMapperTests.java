package com.excentria_it.wamya.adapter.persistence.mapper;

import static com.excentria_it.wamya.test.data.common.GenderJpaTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GenderJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.test.data.common.InternationalCallingCodeJpaEntityTestData;
import com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData;
import com.excentria_it.wamya.test.data.common.UserAccountTestData;

public class UserAccountMapperTests {

	private UserAccountMapper userAccountMapper = new UserAccountMapper();

	@Test
	void testMapClientUserAccountToJpaEntity() {

		UserAccount userAccount = UserAccountTestData.defaultUserAccountBuilder().isTransporter(false).build();
		InternationalCallingCodeJpaEntity iccEntity = InternationalCallingCodeJpaEntityTestData
				.defaultExistentInternationalCallingCodeJpaEntity();
		GenderJpaEntity gender = defaultGenderJpaEntity();
		UserAccountJpaEntity userAccountJpaEntity = userAccountMapper.mapToJpaEntity(userAccount, iccEntity,gender);

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
		GenderJpaEntity gender = defaultGenderJpaEntity();
		UserAccountJpaEntity userAccountJpaEntity = userAccountMapper.mapToJpaEntity(userAccount, iccEntity,gender);

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
		GenderJpaEntity gender = defaultGenderJpaEntity();
		UserAccountJpaEntity userAccountJpaEntity = userAccountMapper.mapToJpaEntity(userAccount, iccEntity, gender);

		assertNotNull(userAccountJpaEntity.getCreationDateTime());

	}

	@Test
	void testMapToJpaEntityWithNullTransporterAccountCreationDateTime() {

		UserAccount userAccount = UserAccountTestData.defaultUserAccountBuilder().isTransporter(true)
				.creationDateTime(null).build();
		InternationalCallingCodeJpaEntity iccEntity = InternationalCallingCodeJpaEntityTestData
				.defaultExistentInternationalCallingCodeJpaEntity();
		GenderJpaEntity gender = defaultGenderJpaEntity();
		UserAccountJpaEntity userAccountJpaEntity = userAccountMapper.mapToJpaEntity(userAccount, iccEntity, gender);

		assertNotNull(userAccountJpaEntity.getCreationDateTime());

	}

	@Test
	void testMapToJpaEntityWithNullUserAccount() {

		UserAccount userAccount = null;
		InternationalCallingCodeJpaEntity iccEntity = InternationalCallingCodeJpaEntityTestData
				.defaultExistentInternationalCallingCodeJpaEntity();
		GenderJpaEntity gender = defaultGenderJpaEntity();
		UserAccountJpaEntity userAccountJpaEntity = userAccountMapper.mapToJpaEntity(userAccount, iccEntity, gender);

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

}
