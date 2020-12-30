package com.excentria_it.wamya.adapter.persistence.mapper;

import static com.excentria_it.wamya.test.data.common.InternationalCallingCodeJpaEntityTestData.*;
import static com.excentria_it.wamya.test.data.common.UserAccountTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.domain.UserAccount;

public class ClientMapperTests {
	private ClientMapper clientMapper = new ClientMapper();

	@Test
	void testMapToJpaEntity() {
		UserAccount userAccount = defaultUserAccountBuilder().build();
		InternationalCallingCodeJpaEntity icc = defaultExistentInternationalCallingCodeJpaEntity();
		ClientJpaEntity clientJpaEntity = clientMapper.mapToJpaEntity(userAccount, icc);

		assertEquals(userAccount.getId(), clientJpaEntity.getId());
		assertEquals(userAccount.getOauthId(), clientJpaEntity.getOauthId());
		assertEquals(userAccount.getIsTransporter(), false);
		assertEquals(userAccount.getGender(), clientJpaEntity.getGender());
		assertEquals(userAccount.getFirstname(), clientJpaEntity.getFirstname());
		assertEquals(userAccount.getLastname(), clientJpaEntity.getLastname());
		assertEquals(userAccount.getDateOfBirth(), clientJpaEntity.getDateOfBirth());
		assertEquals(userAccount.getEmail(), clientJpaEntity.getEmail());
		assertEquals(userAccount.getEmailValidationCode(), clientJpaEntity.getEmailValidationCode());
		assertEquals(userAccount.getIsValidatedEmail(), clientJpaEntity.getIsValidatedEmail());
		assertEquals(
				userAccount.getMobilePhoneNumber().getInternationalCallingCode() + "_"
						+ userAccount.getMobilePhoneNumber().getMobileNumber(),
				clientJpaEntity.getIcc().getValue() + "_" + clientJpaEntity.getMobileNumber());
		assertEquals(userAccount.getMobileNumberValidationCode(), clientJpaEntity.getMobileNumberValidationCode());
		assertEquals(userAccount.getIsValidatedMobileNumber(), clientJpaEntity.getIsValidatedMobileNumber());

		assertEquals(userAccount.getReceiveNewsletter(), clientJpaEntity.getReceiveNewsletter());
		assertEquals(userAccount.getCreationDateTime(), clientJpaEntity.getCreationDateTime());
		assertEquals(userAccount.getPhotoUrl(), clientJpaEntity.getPhotoUrl());
	}

	@Test
	void testMapNullToJpaEntity() {

		InternationalCallingCodeJpaEntity icc = defaultExistentInternationalCallingCodeJpaEntity();
		ClientJpaEntity clientJpaEntity = clientMapper.mapToJpaEntity(null, icc);
		assertNull(clientJpaEntity);

	}
	
	@Test
	void testMapNotClientToJpaEntity() {
		UserAccount userAccount = defaultUserAccountBuilder().isTransporter(true).build();
		InternationalCallingCodeJpaEntity icc = defaultExistentInternationalCallingCodeJpaEntity();
		ClientJpaEntity clientJpaEntity = clientMapper.mapToJpaEntity(userAccount, icc);
		assertNull(clientJpaEntity);

	}
}
