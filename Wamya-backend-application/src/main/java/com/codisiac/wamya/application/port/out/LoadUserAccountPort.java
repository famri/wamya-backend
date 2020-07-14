package com.codisiac.wamya.application.port.out;

import java.util.Optional;

import com.codisiac.wamya.domain.UserAccount;
import com.codisiac.wamya.domain.UserAccount.MobilePhoneNumber;

public interface LoadUserAccountPort {

	Optional<UserAccount> loadUserAccount(MobilePhoneNumber mobilePhoneNumber);
}
