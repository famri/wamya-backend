package com.excentria_it.wamya.application.port.out;

import java.util.Optional;

import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.domain.UserAccount.MobilePhoneNumber;

public interface LoadUserAccountPort {

	Optional<UserAccount> loadUserAccount(MobilePhoneNumber mobilePhoneNumber);
}
