package com.excentria_it.wamya.adapter.persistence.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.test.data.common.InternationalCallingCodeJpaEntityTestData;
import com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData;
import com.excentria_it.wamya.test.data.common.UserAccountTestData;

public class UserAccountMapperTests {

	private UserAccountMapper userAccountMapper = new UserAccountMapper();

	@Test
	void testMapToJpaEntity() {

		UserAccount userAccount = UserAccountTestData.defaultUserAccountBuilder().build();
		InternationalCallingCodeJpaEntity iccEntity = InternationalCallingCodeJpaEntityTestData
				.defaultExistentInternationalCallingCodeJpaEntity();
		UserAccountJpaEntity userAccountJpaEntity = userAccountMapper.mapToJpaEntity(userAccount, iccEntity);

		assertEquals(userAccount.getId(), userAccountJpaEntity.getId());

		assertEquals(userAccount.getIsTransporter(), userAccountJpaEntity.getIsTransporter());

		assertEquals(userAccount.getGender(), userAccountJpaEntity.getGender());

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

		assertEquals(userAccount.getCreationDateTime(), userAccountJpaEntity.getCreationDateTime());

	}

	@Test
	void testMapToJpaEntityWithNullUserAccountCreationDateTime() {

		UserAccount userAccount = UserAccountTestData.defaultUserAccountBuilder().creationDateTime(null).build();
		InternationalCallingCodeJpaEntity iccEntity = InternationalCallingCodeJpaEntityTestData
				.defaultExistentInternationalCallingCodeJpaEntity();
		UserAccountJpaEntity userAccountJpaEntity = userAccountMapper.mapToJpaEntity(userAccount, iccEntity);

		assertNotNull(userAccountJpaEntity.getCreationDateTime());

	}

	@Test
	void testMapToJpaEntityWithNullUserAccount() {

		UserAccount userAccount = null;
		InternationalCallingCodeJpaEntity iccEntity = InternationalCallingCodeJpaEntityTestData
				.defaultExistentInternationalCallingCodeJpaEntity();
		UserAccountJpaEntity userAccountJpaEntity = userAccountMapper.mapToJpaEntity(userAccount, iccEntity);

		assertNull(userAccountJpaEntity);

	}

	@Test
	void testMapToDomainEntity() {

		UserAccountJpaEntity userAccountJpaEntity = UserAccountJpaEntityTestData
				.defaultExistingNotTransporterUserAccountJpaEntity();

		UserAccount userAccount = userAccountMapper.mapToDomainEntity(userAccountJpaEntity);

		assertEquals(userAccount.getId(), userAccountJpaEntity.getId());

		assertEquals(userAccount.getIsTransporter(), userAccountJpaEntity.getIsTransporter());

		assertEquals(userAccount.getGender(), userAccountJpaEntity.getGender());

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

		assertEquals(userAccount.getCreationDateTime(), userAccountJpaEntity.getCreationDateTime());

	}

	@Test
	void testMapToDomainEntityFromNullUserAccountJpaEntity() {

		UserAccount userAccount = userAccountMapper.mapToDomainEntity(null);

		assertNull(userAccount);

	}

}
