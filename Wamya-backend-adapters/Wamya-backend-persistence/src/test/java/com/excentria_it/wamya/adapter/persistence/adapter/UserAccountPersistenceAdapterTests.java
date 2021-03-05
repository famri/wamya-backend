package com.excentria_it.wamya.adapter.persistence.adapter;

import static com.excentria_it.wamya.test.data.common.UserPreferenceJpaTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserPreferenceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.ClientMapper;
import com.excentria_it.wamya.adapter.persistence.mapper.TransporterMapper;
import com.excentria_it.wamya.adapter.persistence.mapper.UserAccountMapper;
import com.excentria_it.wamya.adapter.persistence.mapper.UserPreferenceMapper;
import com.excentria_it.wamya.adapter.persistence.repository.ClientRepository;
import com.excentria_it.wamya.adapter.persistence.repository.InternationalCallingCodeRepository;
import com.excentria_it.wamya.adapter.persistence.repository.TransporterRepository;
import com.excentria_it.wamya.adapter.persistence.repository.UserAccountRepository;
import com.excentria_it.wamya.common.exception.UnsupportedInternationalCallingCode;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.domain.UserAccount.UserAccountBuilder;
import com.excentria_it.wamya.domain.UserPreference;
import com.excentria_it.wamya.test.data.common.InternationalCallingCodeJpaEntityTestData;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData;
import com.excentria_it.wamya.test.data.common.UserAccountTestData;

@ExtendWith(MockitoExtension.class)
public class UserAccountPersistenceAdapterTests {

	@Mock
	private ClientRepository clientRepository;
	@Mock
	private TransporterRepository transporterRepository;
	@Mock
	private UserAccountRepository userAccountRepository;
	@Mock
	private InternationalCallingCodeRepository iccRepository;
	@Mock
	private UserAccountMapper userAccountMapper;
	@Mock
	private TransporterMapper transporterMapper;
	@Mock
	private ClientMapper clientMapper;
	@Mock
	private UserPreferenceMapper userPreferenceMapper;

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
	void givenFoundIcc_WhenCreateTransporterUserAccount_ThenSaveTransporterJpaEntity() {

		UserAccountBuilder userAccountBuilder = UserAccountTestData.defaultUserAccountBuilder();
		UserAccount userAccount = userAccountBuilder.isTransporter(true).build();

		Optional<InternationalCallingCodeJpaEntity> iccEntity = Optional
				.of(InternationalCallingCodeJpaEntityTestData.defaultExistentInternationalCallingCodeJpaEntity());
		given(iccRepository.findByValue(userAccount.getMobilePhoneNumber().getInternationalCallingCode()))
				.willReturn(iccEntity);

		TransporterJpaEntity transporterJpaEntity = UserAccountJpaEntityTestData.defaultNewTransporterJpaEntity();

		given(transporterMapper.mapToJpaEntity(any(UserAccount.class), any(InternationalCallingCodeJpaEntity.class)))
				.willReturn(transporterJpaEntity);
		given(transporterRepository.save(transporterJpaEntity)).willReturn(transporterJpaEntity);

		userAccountPersistenceAdapter.createUserAccount(userAccount);

		then(transporterRepository).should(times(1)).save(transporterJpaEntity);
	}

	@Test
	void givenFoundIcc_WhenCreateClientUserAccount_ThenSaveTransporterJpaEntity() {

		UserAccountBuilder userAccountBuilder = UserAccountTestData.defaultUserAccountBuilder();
		UserAccount userAccount = userAccountBuilder.isTransporter(false).build();

		Optional<InternationalCallingCodeJpaEntity> iccEntity = Optional
				.of(InternationalCallingCodeJpaEntityTestData.defaultExistentInternationalCallingCodeJpaEntity());
		given(iccRepository.findByValue(userAccount.getMobilePhoneNumber().getInternationalCallingCode()))
				.willReturn(iccEntity);

		ClientJpaEntity clientJpaEntity = UserAccountJpaEntityTestData.defaultNewClientJpaEntity();

		given(clientMapper.mapToJpaEntity(any(UserAccount.class), any(InternationalCallingCodeJpaEntity.class)))
				.willReturn(clientJpaEntity);
		given(clientRepository.save(clientJpaEntity)).willReturn(clientJpaEntity);

		userAccountPersistenceAdapter.createUserAccount(userAccount);

		then(clientRepository).should(times(1)).save(clientJpaEntity);
	}

	// Test loadUserAccountByIccAndMobileNumber

	@Test
	void givenExistentUserAccountByIccAndMobileNumber_WhenLoadUserAccountByIccAndMobileNumber_ThenReturnTheExistingUserAccount() {

		// given
		UserAccountJpaEntity userAccountEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();

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
				.ofNullable(UserAccountJpaEntityTestData.defaultExistentClientJpaEntity());

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
				.ofNullable(UserAccountJpaEntityTestData.defaultExistentClientJpaEntity());

		given(userAccountRepository.findById(userAccount.getId())).willReturn(userAccountJpaEntityOptional);

		InternationalCallingCodeJpaEntity iccEntity = InternationalCallingCodeJpaEntityTestData
				.defaultExistentInternationalCallingCodeJpaEntity();
		Optional<InternationalCallingCodeJpaEntity> iccEntityOptional = Optional.ofNullable(iccEntity);

		given(iccRepository.findByValue(userAccount.getMobilePhoneNumber().getInternationalCallingCode()))
				.willReturn(iccEntityOptional);

		given(userAccountMapper.mapToJpaEntity(any(UserAccount.class), any(InternationalCallingCodeJpaEntity.class)))
				.willReturn(userAccountJpaEntityOptional.get());

		// when
		userAccountPersistenceAdapter.updateUserAccount(userAccount);

		// then
		then(userAccountRepository).should(times(1)).save(userAccountJpaEntityOptional.get());
	}

	// Test loadUserAccountByEmail
	@Test
	void givenExistentUserAccountByEmail_WhenLoadUserAccountByIccAndMobileNumber_ThenReturnTheExistingUserAccount() {

		// given
		UserAccountJpaEntity clientEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();

		Optional<UserAccountJpaEntity> clientEntityOptional = Optional.ofNullable(clientEntity);

		given(userAccountRepository.findByEmail(TestConstants.DEFAULT_EMAIL)).willReturn(clientEntityOptional);

		UserAccount userAccount = UserAccountTestData.defaultUserAccountBuilder().build();

		given(userAccountMapper.mapToDomainEntity(clientEntity)).willReturn(userAccount);

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

	@Test
	void loadUserPreferencesByIccAndMobileNumber() {
		// given
		Set<UserPreferenceJpaEntity> userPereferencesJpaEntities = defaultUserPreferenceJpaEntities();

		given(userAccountRepository.loadUserPreferencesByMobilePhoneNumber(any(String.class), any(String.class)))
				.willReturn(userPereferencesJpaEntities);

		List<UserPreference> userPreferences = new ArrayList<>();
		int i = 0;
		for (UserPreferenceJpaEntity e : userPereferencesJpaEntities) {
			userPreferences.add(new UserPreference(e.getId(), e.getKey(), e.getValue()));
			given(userPreferenceMapper.mapToDomainEntity(e)).willReturn(userPreferences.get(i));
		}

		String[] mobileNumber = TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME.split("_");
		// when

		Set<UserPreference> result = userAccountPersistenceAdapter
				.loadUserPreferencesByIccAndMobileNumber(mobileNumber[0], mobileNumber[1]);
		// then
		assertThat(result).containsAll(userPreferences);

	}

	@Test
	void loadUserPreferencesByEmail() {
		// given
		Set<UserPreferenceJpaEntity> userPereferencesJpaEntities = defaultUserPreferenceJpaEntities();

		given(userAccountRepository.loadUserPreferencesByEmail(any(String.class)))
				.willReturn(userPereferencesJpaEntities);

		List<UserPreference> userPreferences = new ArrayList<>();
		int i = 0;
		for (UserPreferenceJpaEntity e : userPereferencesJpaEntities) {
			userPreferences.add(new UserPreference(e.getId(), e.getKey(), e.getValue()));
			given(userPreferenceMapper.mapToDomainEntity(e)).willReturn(userPreferences.get(i));
		}

		// when

		Set<UserPreference> result = userAccountPersistenceAdapter
				.loadUserPreferencesByEmail(TestConstants.DEFAULT_EMAIL);
		// then
		assertThat(result).containsAll(userPreferences);

	}

	@Test
	void loadUserPreferenceByIccAndMobileNumberAndKey() {
		// given
		UserPreferenceJpaEntity userPereferencesJpaEntity = defaultUserTimeZonePreferenceJpaEntity();
		UserPreference userPreference = new UserPreference(userPereferencesJpaEntity.getId(),
				userPereferencesJpaEntity.getKey(), userPereferencesJpaEntity.getValue());
		given(userAccountRepository.loadUserPreferencesByMobilePhoneNumberAndKey(any(String.class), any(String.class),
				any(String.class))).willReturn(Optional.of(userPereferencesJpaEntity));

		given(userPreferenceMapper.mapToDomainEntity(userPereferencesJpaEntity)).willReturn(userPreference);

		String[] mobileNumber = TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME.split("_");
		// when

		Optional<UserPreference> result = userAccountPersistenceAdapter
				.loadUserPreferenceByIccAndMobileNumberAndKey(mobileNumber[0], mobileNumber[1], "timezone");
		// then
		assertEquals(result.get(), userPreference);

	}

	@Test
	void loadUserPreferenceByIccAndMobileNumberAndKeyWithEmptyEntity() {
		// given

		given(userAccountRepository.loadUserPreferencesByMobilePhoneNumberAndKey(any(String.class), any(String.class),
				any(String.class))).willReturn(Optional.empty());

		String[] mobileNumber = TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME.split("_");
		// when

		Optional<UserPreference> result = userAccountPersistenceAdapter
				.loadUserPreferenceByIccAndMobileNumberAndKey(mobileNumber[0], mobileNumber[1], "timezone");
		// then
		assertThat(result).isEmpty();

	}

	@Test
	void loadUserPreferenceByEmailAndKey() {
		// given
		UserPreferenceJpaEntity userPereferencesJpaEntity = defaultUserTimeZonePreferenceJpaEntity();
		UserPreference userPreference = new UserPreference(userPereferencesJpaEntity.getId(),
				userPereferencesJpaEntity.getKey(), userPereferencesJpaEntity.getValue());
		given(userAccountRepository.loadUserPreferencesByEmailAndKey(any(String.class), any(String.class)))
				.willReturn(Optional.of(userPereferencesJpaEntity));

		given(userPreferenceMapper.mapToDomainEntity(userPereferencesJpaEntity)).willReturn(userPreference);

		// when

		Optional<UserPreference> result = userAccountPersistenceAdapter
				.loadUserPreferenceByEmailAndKey(TestConstants.DEFAULT_EMAIL, "timezone");
		// then
		assertEquals(result.get(), userPreference);

	}

	@Test
	void loadUserPreferenceByEmailAndKeyWithEmptyEntity() {
		// given

		given(userAccountRepository.loadUserPreferencesByEmailAndKey(any(String.class), any(String.class)))
				.willReturn(Optional.empty());

		// when

		Optional<UserPreference> result = userAccountPersistenceAdapter
				.loadUserPreferenceByEmailAndKey(TestConstants.DEFAULT_EMAIL, "timezone");
		// then
		assertThat(result).isEmpty();

	}
}
