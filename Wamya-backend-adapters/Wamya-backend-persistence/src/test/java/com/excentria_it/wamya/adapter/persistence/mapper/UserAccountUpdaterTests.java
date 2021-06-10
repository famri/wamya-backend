package com.excentria_it.wamya.adapter.persistence.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.adapter.persistence.entity.GenderJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.adapter.persistence.repository.GenderRepository;
import com.excentria_it.wamya.adapter.persistence.repository.InternationalCallingCodeRepository;
import com.excentria_it.wamya.common.exception.GenderNotFoundException;
import com.excentria_it.wamya.common.exception.UnsupportedInternationalCallingCodeException;
import com.excentria_it.wamya.domain.ValidationState;
import com.excentria_it.wamya.test.data.common.GenderJpaTestData;
import com.excentria_it.wamya.test.data.common.InternationalCallingCodeJpaEntityTestData;
import com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData;

@ExtendWith(MockitoExtension.class)
public class UserAccountUpdaterTests {

	@Mock
	private GenderRepository genderRepository;
	@Mock
	private InternationalCallingCodeRepository iccRepository;

	@InjectMocks
	private UserAccountUpdater userAccountUpdater;

	@Test
	void givenBadGenderId_whenUpdateAboutSection_thenThrowGenderNotFoundException() {
		// given
		UserAccountJpaEntity userAccountJpaEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();
		given(genderRepository.findById(any(Long.class))).willReturn(Optional.empty());
		// when // then
		assertThrows(GenderNotFoundException.class, () -> userAccountUpdater.updateAboutSection(userAccountJpaEntity,
				2L, "newFirstName", "newLastName", LocalDate.of(2000, 10, 01), "new mini bio"));

	}

	@Test
	void givenExistentGender_whenUpdateAboutSection_thenUpdateAboutSection() {
		// given
		UserAccountJpaEntity userAccountJpaEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();
		GenderJpaEntity genderJpaEntity = GenderJpaTestData.womanGenderJpaEntity();

		given(genderRepository.findById(any(Long.class))).willReturn(Optional.of(genderJpaEntity));

		LocalDate newDateOfBirth = LocalDate.of(2000, 10, 01);
		// when
		UserAccountJpaEntity newUserAccountJpaEntity = userAccountUpdater.updateAboutSection(userAccountJpaEntity, 2L,
				"newFirstName", "newLastName", newDateOfBirth, "new mini bio");
		// then

		assertEquals(genderJpaEntity, newUserAccountJpaEntity.getGender());

		assertEquals("newFirstName", newUserAccountJpaEntity.getFirstname());
		assertEquals("newLastName", newUserAccountJpaEntity.getLastname());
		assertEquals(newDateOfBirth, newUserAccountJpaEntity.getDateOfBirth());
		assertEquals("new mini bio", newUserAccountJpaEntity.getMiniBio());

		assertEquals(ValidationState.NOT_VALIDATED, newUserAccountJpaEntity.getIdentityValidationState());
	}

	@Test
	void givenExistentGenderAndNoFirsnnameAndLAstnameUpdates_whenUpdateAboutSection_thenUpdateAboutSection() {
		// given
		UserAccountJpaEntity userAccountJpaEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();
		GenderJpaEntity genderJpaEntity = GenderJpaTestData.womanGenderJpaEntity();

		given(genderRepository.findById(any(Long.class))).willReturn(Optional.of(genderJpaEntity));

		LocalDate newDateOfBirth = LocalDate.of(2000, 10, 01);
		// when
		UserAccountJpaEntity newUserAccountJpaEntity = userAccountUpdater.updateAboutSection(userAccountJpaEntity, 2L,
				userAccountJpaEntity.getFirstname(), userAccountJpaEntity.getLastname(), newDateOfBirth,
				"new mini bio");
		// then

		assertEquals(genderJpaEntity, newUserAccountJpaEntity.getGender());

		assertEquals(userAccountJpaEntity.getFirstname(), newUserAccountJpaEntity.getFirstname());
		assertEquals(userAccountJpaEntity.getLastname(), newUserAccountJpaEntity.getLastname());
		assertEquals(newDateOfBirth, newUserAccountJpaEntity.getDateOfBirth());
		assertEquals("new mini bio", newUserAccountJpaEntity.getMiniBio());

		assertEquals(userAccountJpaEntity.getIdentityValidationState(),
				newUserAccountJpaEntity.getIdentityValidationState());
	}

	@Test
	void testUpdateEmailSection() {
		// given
		UserAccountJpaEntity userAccountJpaEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();

		// when
		UserAccountJpaEntity newUserAccountJpaEntity = userAccountUpdater.updateEmailSection(userAccountJpaEntity,
				"new-email@gmail.com");
		// then

		assertEquals("new-email@gmail.com", newUserAccountJpaEntity.getEmail());
		assertEquals(false, newUserAccountJpaEntity.getIsValidatedEmail());

	}

	@Test
	void testUpdateEmailSectionWithSameEmail() {
		// given
		UserAccountJpaEntity userAccountJpaEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();

		// when
		UserAccountJpaEntity newUserAccountJpaEntity = userAccountUpdater.updateEmailSection(userAccountJpaEntity,
				userAccountJpaEntity.getEmail());
		// then

		assertEquals(userAccountJpaEntity.getEmail(), newUserAccountJpaEntity.getEmail());
		assertEquals(userAccountJpaEntity.getIsValidatedEmail(), newUserAccountJpaEntity.getIsValidatedEmail());

	}

	@Test
	void givenBadIccId_whenUpdateMobileSection_thenThrowUnsupportedInternationalCallingCodeException() {
		// given
		UserAccountJpaEntity userAccountJpaEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();
		given(iccRepository.findById(any(Long.class))).willReturn(Optional.empty());
		// when // then
		assertThrows(UnsupportedInternationalCallingCodeException.class,
				() -> userAccountUpdater.updateMobileSection(userAccountJpaEntity, "22222222", 10L));

	}

	@Test
	void givenExistentIcc_whenUpdateMobileSection_thenUpdateMobileSection() {
		// given
		UserAccountJpaEntity userAccountJpaEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();
		InternationalCallingCodeJpaEntity iccEntity = InternationalCallingCodeJpaEntityTestData
				.defaultExistentInternationalCallingCodeJpaEntity();
		given(iccRepository.findById(any(Long.class))).willReturn(Optional.of(iccEntity));

		// when
		UserAccountJpaEntity newUserAccountJpaEntity = userAccountUpdater.updateMobileSection(userAccountJpaEntity,
				"22222222", 10L);
		// then

		assertEquals(iccEntity, newUserAccountJpaEntity.getIcc());

		assertEquals("22222222", newUserAccountJpaEntity.getMobileNumber());
		assertEquals(false, newUserAccountJpaEntity.getIsValidatedMobileNumber());
	}

	@Test
	void givenExistentIcc_whenUpdateMobileSectionWithSameMobileNumberAndIcc_thenUpdateMobileSection() {
		// given
		UserAccountJpaEntity userAccountJpaEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();

		// when
		UserAccountJpaEntity newUserAccountJpaEntity = userAccountUpdater.updateMobileSection(userAccountJpaEntity,
				userAccountJpaEntity.getMobileNumber(), userAccountJpaEntity.getIcc().getId());
		// then

		assertEquals(userAccountJpaEntity.getIcc(), newUserAccountJpaEntity.getIcc());

		assertEquals(userAccountJpaEntity.getMobileNumber(), newUserAccountJpaEntity.getMobileNumber());
		assertEquals(true, newUserAccountJpaEntity.getIsValidatedMobileNumber());
	}
}
