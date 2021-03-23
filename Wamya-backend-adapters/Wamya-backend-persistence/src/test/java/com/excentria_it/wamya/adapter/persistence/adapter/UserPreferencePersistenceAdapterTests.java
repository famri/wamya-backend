package com.excentria_it.wamya.adapter.persistence.adapter;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.adapter.persistence.repository.UserAccountRepository;
import com.excentria_it.wamya.adapter.persistence.repository.UserPreferenceRepository;
import com.excentria_it.wamya.domain.UserPreference;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData;

@ExtendWith(MockitoExtension.class)
public class UserPreferencePersistenceAdapterTests {
	@Mock
	private UserAccountRepository userAccountRepository;
	@Mock
	private UserPreferenceRepository userPreferenceRepository;

	@InjectMocks
	private UserPreferencePersistenceAdapter userPreferencePersistenceAdapter;

	@Test
	void testSaveUserPreferenceWithEmailUsername() {
		// given
		UserAccountJpaEntity userAccountEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();
		given(userAccountRepository.findByEmailWithUserPreferences(any(String.class)))
				.willReturn(Optional.of(userAccountEntity));

		// When
		userPreferencePersistenceAdapter.saveUserPreference("SOME_KEY", "SOME_VALUE", TestConstants.DEFAULT_EMAIL);
		// then
		ArgumentCaptor<UserAccountJpaEntity> captor = ArgumentCaptor.forClass(UserAccountJpaEntity.class);

		then(userAccountRepository).should(times(1)).save(captor.capture());

		assertTrue(captor.getValue().getPreferences().get("SOME_KEY") != null
				&& "SOME_VALUE".equals(captor.getValue().getPreferences().get("SOME_KEY").getValue()));
		assertTrue(captor.getValue().getPreferences().get("SOME_KEY") != null
				&& captor.getValue().getPreferences().get("SOME_KEY").getUserAccount().equals(userAccountEntity));
	}

	@Test
	void testSaveUserPreferenceWithMobilePhoneNumberUsername() {
		// given
		UserAccountJpaEntity userAccountEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();
		given(userAccountRepository.findByMobilePhoneNumberWithUserPreferences(any(String.class), any(String.class)))
				.willReturn(Optional.of(userAccountEntity));

		// When
		userPreferencePersistenceAdapter.saveUserPreference("SOME_KEY", "SOME_VALUE",
				TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME);
		// then
		ArgumentCaptor<UserAccountJpaEntity> captor = ArgumentCaptor.forClass(UserAccountJpaEntity.class);

		then(userAccountRepository).should(times(1)).save(captor.capture());

		assertTrue(captor.getValue().getPreferences().get("SOME_KEY") != null
				&& "SOME_VALUE".equals(captor.getValue().getPreferences().get("SOME_KEY").getValue()));
		assertTrue(captor.getValue().getPreferences().get("SOME_KEY") != null
				&& captor.getValue().getPreferences().get("SOME_KEY").getUserAccount().equals(userAccountEntity));
	}

	@Test
	void testSaveUserPreferenceWithBadUsername() {
		// given

		// When
		userPreferencePersistenceAdapter.saveUserPreference("SOME_KEY", "SOME_VALUE", "BAD USER NAME");
		// then
		then(userAccountRepository).should(never()).save(any(UserAccountJpaEntity.class));

	}

	@Test
	void givenUserAccountNotFoundByUserName_WehnSaveUserPreference_ThenDoNotSavePreference() {
		// given

		given(userAccountRepository.findByEmailWithUserPreferences(any(String.class))).willReturn(Optional.empty());

		// When
		userPreferencePersistenceAdapter.saveUserPreference("SOME_KEY", "SOME_VALUE", TestConstants.DEFAULT_EMAIL);
		// then

		then(userAccountRepository).should(never()).save(any(UserAccountJpaEntity.class));

	}

	@Test
	void givenEmailUsernameAndExistentUserPreference_WhenLoadUserPreferenceByKeyAndUsername_ThenReturnUserPreference() {

		// given
		UserPreference userPreference = new UserPreference(1L, "timezone", "Africa/tunis");
		given(userPreferenceRepository.findByKeyAndUserAccountEmail(any(String.class), any(String.class)))
				.willReturn(Optional.of(userPreference));
		// when
		Optional<UserPreference> userPreferenceOptional = userPreferencePersistenceAdapter
				.loadUserPreferenceByKeyAndUsername("timezone", TestConstants.DEFAULT_EMAIL);
		// Then
		then(userPreferenceRepository).should(times(1)).findByKeyAndUserAccountEmail(eq("timezone"),
				eq(TestConstants.DEFAULT_EMAIL));
		assertEquals(userPreference, userPreferenceOptional.get());

	}

	@Test
	void givenMobileNumberUsernameAndExistentUserPreference_WhenLoadUserPreferenceByKeyAndUsername_ThenReturnUserPreference() {

		// given
		UserPreference userPreference = new UserPreference(1L, "timezone", "Africa/tunis");
		given(userPreferenceRepository.findByKeyAndUserAccountMobileNumber(any(String.class), any(String.class),
				any(String.class))).willReturn(Optional.of(userPreference));
		// when
		Optional<UserPreference> userPreferenceOptional = userPreferencePersistenceAdapter
				.loadUserPreferenceByKeyAndUsername("timezone", TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME);
		// Then

		then(userPreferenceRepository).should(times(1)).findByKeyAndUserAccountMobileNumber(eq("timezone"),
				eq(TestConstants.DEFAULT_INTERNATIONAL_CALLING_CODE), eq(TestConstants.DEFAULT_MOBILE_NUMBER));
		assertEquals(userPreference, userPreferenceOptional.get());

	}

	@Test
	void givenBadUsername_WhenLoadUserPreferenceByKeyAndUsername_ThenReturnEmptyUserPreference() {

		// given

		// when
		Optional<UserPreference> userPreferenceOptional = userPreferencePersistenceAdapter
				.loadUserPreferenceByKeyAndUsername("timezone", "BAD USERNAME");
		// Then
		then(userPreferenceRepository).should(never()).findByKeyAndUserAccountEmail(any(String.class),
				any(String.class));
		assertThat(userPreferenceOptional).isEmpty();

	}
}
