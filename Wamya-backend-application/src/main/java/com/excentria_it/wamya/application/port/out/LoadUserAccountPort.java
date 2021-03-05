package com.excentria_it.wamya.application.port.out;

import java.util.Optional;
import java.util.Set;

import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.domain.UserPreference;

public interface LoadUserAccountPort {

	Optional<UserAccount> loadUserAccountByIccAndMobileNumber(String icc, String mobileNumber);

	Optional<UserAccount> loadUserAccountByEmail(String email);

	Set<UserPreference> loadUserPreferencesByIccAndMobileNumber(String icc, String mobileNumber);

	Set<UserPreference> loadUserPreferencesByEmail(String email);

	Optional<UserPreference> loadUserPreferenceByIccAndMobileNumberAndKey(String icc, String mobileNumber, String key);

	Optional<UserPreference> loadUserPreferenceByEmailAndKey(String email, String key);
}
