package com.excentria_it.wamya.application.port.out;

import java.util.Optional;

import com.excentria_it.wamya.domain.UserAccount;

public interface LoadUserAccountPort {

	Optional<UserAccount> loadUserAccountByIccAndMobileNumber(String icc, String mobileNumber);

	Optional<UserAccount> loadUserAccountByEmail(String email);
	
	Optional<UserAccount> loadUserAccountByIccAndMobileNumberAndPassword(String icc, String mobileNumber, String password);
	
	Optional<UserAccount> loadUserAccountByEmailAndPassword(String email, String password);
}
