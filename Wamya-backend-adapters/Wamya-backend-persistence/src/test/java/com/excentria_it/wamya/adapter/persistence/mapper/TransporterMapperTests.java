package com.excentria_it.wamya.adapter.persistence.mapper;

import static com.excentria_it.wamya.test.data.common.InternationalCallingCodeJpaEntityTestData.*;
import static com.excentria_it.wamya.test.data.common.UserAccountTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.domain.UserAccount;

public class TransporterMapperTests {

	private TransporterMapper transporterMapper = new TransporterMapper();

	@Test
	void testMapToJpaEntity() {
		UserAccount userAccount = defaultUserAccountBuilder().isTransporter(true).build();
		InternationalCallingCodeJpaEntity icc = defaultExistentInternationalCallingCodeJpaEntity();
		TransporterJpaEntity transporterJpaEntity = transporterMapper.mapToJpaEntity(userAccount, icc);

		assertEquals(userAccount.getId(), transporterJpaEntity.getId());
		assertEquals(userAccount.getOauthId(), transporterJpaEntity.getOauthId());
		assertEquals(userAccount.getIsTransporter(), true);
		assertEquals(userAccount.getGender(), transporterJpaEntity.getGender());
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
		assertEquals(userAccount.getCreationDateTime(), transporterJpaEntity.getCreationDateTime());
		assertEquals(userAccount.getPhotoUrl(), transporterJpaEntity.getPhotoUrl());
	}

	@Test
	void testMapNullToJpaEntity() {
		InternationalCallingCodeJpaEntity icc = defaultExistentInternationalCallingCodeJpaEntity();
		TransporterJpaEntity transporterJpaEntity = transporterMapper.mapToJpaEntity(null, icc);
		assertNull(transporterJpaEntity);
	}
	
	@Test
	void testMapNotTransporterToJpaEntity() {
		UserAccount userAccount = defaultUserAccountBuilder().isTransporter(false).build();
		InternationalCallingCodeJpaEntity icc = defaultExistentInternationalCallingCodeJpaEntity();
		TransporterJpaEntity transporterJpaEntity = transporterMapper.mapToJpaEntity(userAccount, icc);
		assertNull(transporterJpaEntity);
	}
}
