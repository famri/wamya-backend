package com.excentria_it.wamya.adapter.persistence.adapter.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.UserAccountMapper;
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
				.defaultInternationalCallingCodeJpaEntity();
		UserAccountJpaEntity userAccountJpaEntity = userAccountMapper.mapToJpaEntity(userAccount, iccEntity);

		assertEquals(userAccount.getId(), userAccountJpaEntity.getId());

		assertEquals(userAccount.getIsTransporter(), userAccountJpaEntity.getIsTransporter());

		assertEquals(userAccount.getGender(), userAccountJpaEntity.getGender());

		assertEquals(userAccount.getFirstName(), userAccountJpaEntity.getFirstName());

		assertEquals(userAccount.getLastName(), userAccountJpaEntity.getLastName());

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

		assertEquals(userAccount.getCreationTimestamp(), userAccountJpaEntity.getCreationTimestamp());

	}

	@Test
	void testMapToDomainEntity() {

		UserAccountJpaEntity userAccountJpaEntity = UserAccountJpaEntityTestData
				.defaultExistingNotTransporterUserAccountJpaEntity();

		UserAccount userAccount = userAccountMapper.mapToDomainEntity(userAccountJpaEntity);

		assertEquals(userAccount.getId(), userAccountJpaEntity.getId());

		assertEquals(userAccount.getIsTransporter(), userAccountJpaEntity.getIsTransporter());

		assertEquals(userAccount.getGender(), userAccountJpaEntity.getGender());

		assertEquals(userAccount.getFirstName(), userAccountJpaEntity.getFirstName());

		assertEquals(userAccount.getLastName(), userAccountJpaEntity.getLastName());

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

		assertEquals(userAccount.getCreationTimestamp(), userAccountJpaEntity.getCreationTimestamp());

	}

	@Test
	void testMapToDomainEntityFromNullUserAccountJpaEntity() {

		UserAccount userAccount = userAccountMapper.mapToDomainEntity(null);

		assertNull(userAccount);

	}

}
