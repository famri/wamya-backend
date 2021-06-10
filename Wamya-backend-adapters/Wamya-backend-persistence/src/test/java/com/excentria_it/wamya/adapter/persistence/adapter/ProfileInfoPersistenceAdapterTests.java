package com.excentria_it.wamya.adapter.persistence.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.UserAccountMapper;
import com.excentria_it.wamya.adapter.persistence.mapper.UserAccountUpdater;
import com.excentria_it.wamya.adapter.persistence.repository.UserAccountRepository;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.domain.ProfileInfoDto;
import com.excentria_it.wamya.test.data.common.GenderJpaTestData;
import com.excentria_it.wamya.test.data.common.InternationalCallingCodeJpaEntityTestData;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData;
import com.excentria_it.wamya.test.data.common.UserProfileTestData;

@ExtendWith(MockitoExtension.class)
public class ProfileInfoPersistenceAdapterTests {
	@Mock
	private UserAccountRepository userAccountRepository;
	@Mock
	private UserAccountMapper userAccountMapper;
	@Mock
	private UserAccountUpdater userAccountUpdater;
	@InjectMocks
	private ProfileInfoPersistenceAdapter profileInfoPersistenceAdapter;

	@Test
	void givenNullUsername_whenLoadInfo_thenThrowUserAccountNotFoundException() {
		assertThrows(UserAccountNotFoundException.class, () -> profileInfoPersistenceAdapter.loadInfo(null, "fr_FR"));
	}

	@Test
	void givenExistentUserAccountByEmail_whenLoadInfo_thenReturnUserAccount() {
		UserAccountJpaEntity userAccountJpaEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();
		// given
		given(userAccountRepository.findByEmail(any(String.class))).willReturn(Optional.of(userAccountJpaEntity));

		ProfileInfoDto defaultProfileInfoDto = UserProfileTestData.defaultProfileInfoDto();
		given(userAccountMapper.mapToProfileInfoDto(any(UserAccountJpaEntity.class), any(String.class)))
				.willReturn(defaultProfileInfoDto);

		ProfileInfoDto profileInfoDto = profileInfoPersistenceAdapter.loadInfo(userAccountJpaEntity.getEmail(),
				"fr_FR");

		assertEquals(defaultProfileInfoDto, profileInfoDto);
	}

	@Test
	void givenInexistentUserAccountByEmail_whenLoadInfo_thenThrowUserAccountNotFoundException() {
		// given
		given(userAccountRepository.findByEmail(any(String.class))).willReturn(Optional.empty());

		assertThrows(UserAccountNotFoundException.class,
				() -> profileInfoPersistenceAdapter.loadInfo(TestConstants.DEFAULT_EMAIL, "fr_FR"));

	}

	@Test
	void givenInexistentUserAccountByMobileNumber_whenLoadInfo_thenThrowUserAccountNotFoundExceptiont() {
		// given
		given(userAccountRepository.findByMobilePhoneNumber(any(String.class), any(String.class)))
				.willReturn(Optional.empty());

		assertThrows(UserAccountNotFoundException.class,
				() -> profileInfoPersistenceAdapter.loadInfo(TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME, "fr_FR"));

	}

	@Test
	void givenBadUsername_whenLoadInfo_thenThrowUserAccountNotFoundExceptiont() {
		// given

		assertThrows(UserAccountNotFoundException.class,
				() -> profileInfoPersistenceAdapter.loadInfo("thisIsABadUSername", "fr_FR"));

	}

	@Test
	void givenExistentUserAccountByEmail_whenUpdateEmailSection_thenUpdateUserAccountJpaEntity() {
		UserAccountJpaEntity userAccountJpaEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();
		UserAccountJpaEntity newUserAccountJpaEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();
		newUserAccountJpaEntity.setEmail("new-email@gmail.com");
		// given
		given(userAccountRepository.findByEmail(any(String.class))).willReturn(Optional.of(userAccountJpaEntity));

		given(userAccountUpdater.updateEmailSection(any(UserAccountJpaEntity.class), any(String.class)))
				.willReturn(newUserAccountJpaEntity);

		// when
		profileInfoPersistenceAdapter.updateEmailSection(userAccountJpaEntity.getEmail(), "new-email@gmail.com");

		// then
		then(userAccountUpdater).should(times(1)).updateEmailSection(userAccountJpaEntity, "new-email@gmail.com");
		then(userAccountRepository).should(times(1)).save(newUserAccountJpaEntity);
	}

	@Test
	void givenInexistentUserAccountByEmail_whenUpdateEmailSection_thenThrowUserAccountNotFoundException() {

		UserAccountJpaEntity userAccountJpaEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();
		// given
		given(userAccountRepository.findByEmail(any(String.class))).willReturn(Optional.empty());

		// when //then
		assertThrows(UserAccountNotFoundException.class, () -> profileInfoPersistenceAdapter
				.updateEmailSection(userAccountJpaEntity.getEmail(), "new-email@gmail.com"));

	}

	@Test
	void givenInexistentUserAccountByMobileNumber_whenUpdateEmailSection_thenThrowUserAccountNotFoundException() {

		UserAccountJpaEntity userAccountJpaEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();
		// given
		given(userAccountRepository.findByMobilePhoneNumber(any(String.class), any(String.class)))
				.willReturn(Optional.empty());

		// when //then
		assertThrows(UserAccountNotFoundException.class,
				() -> profileInfoPersistenceAdapter.updateEmailSection(
						userAccountJpaEntity.getIcc().getValue() + "_" + userAccountJpaEntity.getMobileNumber(),
						"new-email@gmail.com"));

	}

	@Test
	void givenBadUsername_whenUpdateEmailSection_thenThrowUserAccountNotFoundException() {

		// when //then
		assertThrows(UserAccountNotFoundException.class,
				() -> profileInfoPersistenceAdapter.updateEmailSection("ThisIsABadUsername", "new-email@gmail.com"));

	}

	@Test
	void givenExistentUserAccountByEmail_whenUpdateAboutSection_thenUpdateUserAccountJpaEntity() {
		UserAccountJpaEntity userAccountJpaEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();
		UserAccountJpaEntity newUserAccountJpaEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();
		newUserAccountJpaEntity.setFirstname("newFirstname");
		newUserAccountJpaEntity.setLastname("newLastname");

		LocalDate newDateOfBirth = LocalDate.of(2000, 01, 01);
		newUserAccountJpaEntity.setDateOfBirth(newDateOfBirth);
		newUserAccountJpaEntity.setMiniBio("new mini bio");
		newUserAccountJpaEntity.setGender(GenderJpaTestData.womanGenderJpaEntity());

		// given
		given(userAccountRepository.findByEmail(any(String.class))).willReturn(Optional.of(userAccountJpaEntity));

		given(userAccountUpdater.updateAboutSection(any(UserAccountJpaEntity.class), any(Long.class), any(String.class),
				any(String.class), any(LocalDate.class), any(String.class))).willReturn(newUserAccountJpaEntity);

		// when
		profileInfoPersistenceAdapter.updateAboutSection(userAccountJpaEntity.getEmail(),
				newUserAccountJpaEntity.getGender().getId(), newUserAccountJpaEntity.getFirstname(),
				newUserAccountJpaEntity.getLastname(), newUserAccountJpaEntity.getDateOfBirth(),
				newUserAccountJpaEntity.getMiniBio());

		// then
		then(userAccountUpdater).should(times(1)).updateAboutSection(userAccountJpaEntity,
				newUserAccountJpaEntity.getGender().getId(), newUserAccountJpaEntity.getFirstname(),
				newUserAccountJpaEntity.getLastname(), newUserAccountJpaEntity.getDateOfBirth(),
				newUserAccountJpaEntity.getMiniBio());
		then(userAccountRepository).should(times(1)).save(newUserAccountJpaEntity);
	}

	@Test
	void givenInexistentUserAccountByEmail_whenUpdateAboutSection_thenThrowUserAccountNotFoundException() {

		UserAccountJpaEntity userAccountJpaEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();
		// given
		given(userAccountRepository.findByEmail(any(String.class))).willReturn(Optional.empty());

		// when //then
		assertThrows(UserAccountNotFoundException.class,
				() -> profileInfoPersistenceAdapter.updateAboutSection(userAccountJpaEntity.getEmail(), 2L,
						"newFirstname", "newLastname", LocalDate.of(200, 01, 01), "new mini bio"));

	}

	@Test
	void givenInexistentUserAccountByMobileNumber_whenUpdateAboutSection_thenThrowUserAccountNotFoundException() {

		UserAccountJpaEntity userAccountJpaEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();
		// given
		given(userAccountRepository.findByMobilePhoneNumber(any(String.class), any(String.class)))
				.willReturn(Optional.empty());

		// when //then
		assertThrows(UserAccountNotFoundException.class,
				() -> profileInfoPersistenceAdapter.updateAboutSection(
						userAccountJpaEntity.getIcc().getValue() + "_" + userAccountJpaEntity.getMobileNumber(), 2L,
						"newFirstname", "newLastname", LocalDate.of(200, 01, 01), "new mini bio"));

	}

	@Test
	void givenBadUsername_whenUpdateAboutSection_thenThrowUserAccountNotFoundException() {

		// when //then
		assertThrows(UserAccountNotFoundException.class,
				() -> profileInfoPersistenceAdapter.updateAboutSection("ThisIsABadUsername", 2L, "newFirstname",
						"newLastname", LocalDate.of(200, 01, 01), "new mini bio"));

	}

	@Test
	void givenExistentUserAccountByEmail_whenUpdateMobileSection_thenUpdateUserAccountJpaEntity() {
		UserAccountJpaEntity userAccountJpaEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();
		UserAccountJpaEntity newUserAccountJpaEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();

		InternationalCallingCodeJpaEntity newIcc = InternationalCallingCodeJpaEntityTestData
				.defaultExistentInternationalCallingCodeJpaEntity();
		newIcc.setId(10L);
		newIcc.setValue("+213");

		newUserAccountJpaEntity.setMobileNumber("88888888");
		newUserAccountJpaEntity.setIcc(newIcc);
		// given
		given(userAccountRepository.findByEmail(any(String.class))).willReturn(Optional.of(userAccountJpaEntity));

		given(userAccountUpdater.updateMobileSection(any(UserAccountJpaEntity.class), any(String.class),
				any(Long.class))).willReturn(newUserAccountJpaEntity);

		// when
		profileInfoPersistenceAdapter.updateMobileSection(userAccountJpaEntity.getEmail(),
				newUserAccountJpaEntity.getMobileNumber(), newUserAccountJpaEntity.getIcc().getId());

		// then
		then(userAccountUpdater).should(times(1)).updateMobileSection(userAccountJpaEntity,
				newUserAccountJpaEntity.getMobileNumber(), newUserAccountJpaEntity.getIcc().getId());
		then(userAccountRepository).should(times(1)).save(newUserAccountJpaEntity);
	}

	@Test
	void givenInexistentUserAccountByEmail_whenUpdateMobileSection_thenThrowUserAccountNotFoundException() {

		UserAccountJpaEntity userAccountJpaEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();
		// given
		given(userAccountRepository.findByEmail(any(String.class))).willReturn(Optional.empty());

		// when //then
		assertThrows(UserAccountNotFoundException.class, () -> profileInfoPersistenceAdapter
				.updateMobileSection(userAccountJpaEntity.getEmail(), "88888888", 10L));

	}

	@Test
	void givenInexistentUserAccountByMobileNumber_whenUpdateMobileSection_thenThrowUserAccountNotFoundException() {

		UserAccountJpaEntity userAccountJpaEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();
		// given
		given(userAccountRepository.findByMobilePhoneNumber(any(String.class), any(String.class)))
				.willReturn(Optional.empty());

		// when //then
		assertThrows(UserAccountNotFoundException.class,
				() -> profileInfoPersistenceAdapter.updateMobileSection(
						userAccountJpaEntity.getIcc().getValue() + "_" + userAccountJpaEntity.getMobileNumber(),
						"88888888", 10L));

	}

	@Test
	void givenBadUsername_whenUpdateMobileSection_thenThrowUserAccountNotFoundException() {

		// when //then
		assertThrows(UserAccountNotFoundException.class,
				() -> profileInfoPersistenceAdapter.updateMobileSection("ThisIsABadUsername", "88888888", 10L));

	}
}
