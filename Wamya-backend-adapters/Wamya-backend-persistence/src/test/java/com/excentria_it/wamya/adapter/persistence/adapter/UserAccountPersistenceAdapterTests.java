package com.excentria_it.wamya.adapter.persistence.adapter;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.UserAccountMapper;
import com.excentria_it.wamya.adapter.persistence.repository.InternationalCallingCodeRepository;
import com.excentria_it.wamya.adapter.persistence.repository.UserAccountRepository;
import com.excentria_it.wamya.common.exception.UnsupportedInternationalCallingCode;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.domain.UserAccount.UserAccountBuilder;
import com.excentria_it.wamya.test.data.common.InternationalCallingCodeJpaEntityTestData;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData;
import com.excentria_it.wamya.test.data.common.UserAccountTestData;

@ExtendWith(MockitoExtension.class)
public class UserAccountPersistenceAdapterTests {

	@Mock
	private UserAccountRepository userAccountRepository;
	@Mock
	private InternationalCallingCodeRepository iccRepository;
	@Mock
	private UserAccountMapper userAccountMapper;
	@InjectMocks
	private UserAccountPersistenceAdapter userAccountPersistenceAdapter;

	// Test createUserAccount

	@Test
	void givenNotFoundIcc_WhenCreateUserAccount_ThenThrowUnsupportedInternationalCallingCode() {

		UserAccountBuilder userAccountBuilder = UserAccountTestData.defaultUserAccountBuilder();
		UserAccount userAccount = userAccountBuilder.build();

		given(iccRepository.findByValue(userAccount.getMobilePhoneNumber().getInternationalCallingCode()))
				.willReturn(Optional.ofNullable(null));

		assertThrows(UnsupportedInternationalCallingCode.class,
				() -> userAccountPersistenceAdapter.createUserAccount(userAccount));
	}

	@Test
	void givenFoundIcc_WhenCreateUserAccount_ThenSaveUserAccountJpaEntity() {

		UserAccountBuilder userAccountBuilder = UserAccountTestData.defaultUserAccountBuilder();
		UserAccount userAccount = userAccountBuilder.build();
		Optional<InternationalCallingCodeJpaEntity> iccEntity = Optional
				.of(InternationalCallingCodeJpaEntityTestData.defaultInternationalCallingCodeJpaEntity());
		given(iccRepository.findByValue(userAccount.getMobilePhoneNumber().getInternationalCallingCode()))
				.willReturn(iccEntity);

		UserAccountJpaEntity userAccountJpaEntity = UserAccountJpaEntityTestData
				.defaultNewTransporterUserAccountJpaEntity();

		given(userAccountMapper.mapToJpaEntity(userAccount, iccEntity.get())).willReturn(userAccountJpaEntity);
		given(userAccountRepository.save(userAccountJpaEntity)).willReturn(userAccountJpaEntity);

		userAccountPersistenceAdapter.createUserAccount(userAccount);

		then(userAccountRepository).should(times(1)).save(userAccountJpaEntity);
	}

	// Test loadUserAccountByIccAndMobileNumber

	@Test
	void givenExistentUserAccountByIccAndMobileNumber_WhenLoadUserAccountByIccAndMobileNumber_ThenReturnTheExistingUserAccount() {

		// given
		UserAccountJpaEntity userAccountEntity = UserAccountJpaEntityTestData
				.defaultExistingNotTransporterUserAccountJpaEntity();

		Optional<UserAccountJpaEntity> userAccountEntityOptional = Optional.ofNullable(userAccountEntity);

		given(userAccountRepository.findByMobilePhoneNumber(TestConstants.DEFAULT_INTERNATIONAL_CALLING_CODE,
				TestConstants.DEFAULT_MOBILE_NUMBER)).willReturn(userAccountEntityOptional);

		UserAccount userAccount = UserAccountTestData.defaultUserAccountBuilder().build();

		given(userAccountMapper.mapToDomainEntity(userAccountEntity)).willReturn(userAccount);

		// when
		Optional<UserAccount> result = userAccountPersistenceAdapter.loadUserAccountByIccAndMobileNumber(
				TestConstants.DEFAULT_INTERNATIONAL_CALLING_CODE, TestConstants.DEFAULT_MOBILE_NUMBER);
		// then
		assertEquals(result.get(), userAccount);

	}

	@Test
	void givenNullMobile_WhenLoadUserAccountByIccAndMobileNumber_ThenReturnOptionalOfNull() {
		Optional<UserAccount> result = userAccountPersistenceAdapter
				.loadUserAccountByIccAndMobileNumber(TestConstants.DEFAULT_INTERNATIONAL_CALLING_CODE, null);
		assertThat(result.isEmpty()).isTrue();
	}

	@Test
	void givenNullIcc_WhenLoadUserAccountByIccAndMobileNumber_ThenReturnOptionalOfNull() {
		Optional<UserAccount> result = userAccountPersistenceAdapter.loadUserAccountByIccAndMobileNumber(null,
				TestConstants.DEFAULT_MOBILE_NUMBER);
		assertThat(result.isEmpty()).isTrue();
	}

	@Test
	void givenNullMobileNumberAndNullIcc_WhenLoadUserAccountByIccAndMobileNumber_ThenReturnOptionalOfNull() {
		Optional<UserAccount> result = userAccountPersistenceAdapter.loadUserAccountByIccAndMobileNumber(null, null);
		assertThat(result.isEmpty()).isTrue();
	}

	@Test
	void givenNonExistentUserAccountByIccAndMobileNumber_WhenLoadUserAccountByIccAndMobileNumber_ThenReturnOptionalOfNull() {

		// given

		given(userAccountRepository.findByMobilePhoneNumber(TestConstants.DEFAULT_INTERNATIONAL_CALLING_CODE,
				TestConstants.DEFAULT_MOBILE_NUMBER)).willReturn(Optional.ofNullable(null));

		// when
		Optional<UserAccount> result = userAccountPersistenceAdapter.loadUserAccountByIccAndMobileNumber(
				TestConstants.DEFAULT_INTERNATIONAL_CALLING_CODE, TestConstants.DEFAULT_MOBILE_NUMBER);
		// then
		assertThat(result.isEmpty()).isTrue();

	}

	// Test updateUserAccount
	@Test
	void givenNullUserAccountId_WhenUpdateUserAccount_ThenThrowUnsupportedOperationException() {
		assertThrows(UnsupportedOperationException.class, () -> userAccountPersistenceAdapter
				.updateUserAccount(UserAccountTestData.defaultUserAccountBuilder().id(null).build()));

	}

	@Test
	void givenNonExistentUserAccountById_WhenUpdateUserAccount_ThenThrowUserAccountNotFoundException() {
		// given
		UserAccount userAccount = UserAccountTestData.defaultUserAccountBuilder().build();
		given(userAccountRepository.findById(userAccount.getId())).willReturn(Optional.ofNullable(null));

		// when, then
		assertThrows(UserAccountNotFoundException.class,
				() -> userAccountPersistenceAdapter.updateUserAccount(userAccount));

	}

	@Test
	void givenExistentUserAccountByIdWithNonExistentIcc_WhenUpdateUserAccount_ThenThrowUnsupportedInternationalCallingCode() {
		// given
		UserAccount userAccount = UserAccountTestData.defaultUserAccountBuilder().build();

		Optional<UserAccountJpaEntity> userAccountJpaEntity = Optional
				.ofNullable(UserAccountJpaEntityTestData.defaultExistingNotTransporterUserAccountJpaEntity());

		given(userAccountRepository.findById(userAccount.getId())).willReturn(userAccountJpaEntity);

		given(iccRepository.findByValue(userAccount.getMobilePhoneNumber().getInternationalCallingCode()))
				.willReturn(Optional.ofNullable(null));

		// when, then
		assertThrows(UnsupportedInternationalCallingCode.class,
				() -> userAccountPersistenceAdapter.updateUserAccount(userAccount));

	}

	@Test
	void givenExistentUserAccountByIdWithExistentIcc_WhenUpdateUserAccount_ThenShouldSaveUserAccountJpaEntity() {
		// given
		UserAccount userAccount = UserAccountTestData.defaultUserAccountBuilder().build();

		Optional<UserAccountJpaEntity> userAccountJpaEntityOptional = Optional
				.ofNullable(UserAccountJpaEntityTestData.defaultExistingNotTransporterUserAccountJpaEntity());

		given(userAccountRepository.findById(userAccount.getId())).willReturn(userAccountJpaEntityOptional);

		InternationalCallingCodeJpaEntity iccEntity = InternationalCallingCodeJpaEntityTestData
				.defaultInternationalCallingCodeJpaEntity();
		Optional<InternationalCallingCodeJpaEntity> iccEntityOptional = Optional.ofNullable(iccEntity);

		given(iccRepository.findByValue(userAccount.getMobilePhoneNumber().getInternationalCallingCode()))
				.willReturn(iccEntityOptional);

		given(userAccountMapper.mapToJpaEntity(userAccount, iccEntity)).willReturn(userAccountJpaEntityOptional.get());

		// when
		userAccountPersistenceAdapter.updateUserAccount(userAccount);

		// then
		then(userAccountRepository).should(times(1)).save(userAccountJpaEntityOptional.get());
	}

	// Test loadUserAccountByEmail
	@Test
	void givenExistentUserAccountByEmail_WhenLoadUserAccountByIccAndMobileNumber_ThenReturnTheExistingUserAccount() {

		// given
		UserAccountJpaEntity userAccountEntity = UserAccountJpaEntityTestData
				.defaultExistingNotTransporterUserAccountJpaEntity();

		Optional<UserAccountJpaEntity> userAccountEntityOptional = Optional.ofNullable(userAccountEntity);

		given(userAccountRepository.findByEmail(TestConstants.DEFAULT_EMAIL)).willReturn(userAccountEntityOptional);

		UserAccount userAccount = UserAccountTestData.defaultUserAccountBuilder().build();

		given(userAccountMapper.mapToDomainEntity(userAccountEntity)).willReturn(userAccount);

		// when
		Optional<UserAccount> result = userAccountPersistenceAdapter.loadUserAccountByEmail(userAccount.getEmail());
		// then
		assertEquals(result.get(), userAccount);

	}

	@Test
	void givenNullEmail_WhenLoadUserAccountByIccAndMobileNumber_ThenReturnOptionalOfNull() {
		Optional<UserAccount> result = userAccountPersistenceAdapter.loadUserAccountByEmail(null);
		assertThat(result.isEmpty()).isTrue();
	}

	@Test
	void givenNonExistentUserAccountByEmail_WhenLoadUserAccountByIccAndMobileNumber_ThenReturnOptionalOfNull() {

		// given

		given(userAccountRepository.findByEmail(TestConstants.DEFAULT_EMAIL)).willReturn(Optional.ofNullable(null));

		// when
		Optional<UserAccount> result = userAccountPersistenceAdapter
				.loadUserAccountByEmail(TestConstants.DEFAULT_EMAIL);
		// then
		assertThat(result.isEmpty()).isTrue();

	}

}
