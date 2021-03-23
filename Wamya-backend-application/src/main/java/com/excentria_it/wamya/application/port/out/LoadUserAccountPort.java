package com.excentria_it.wamya.application.port.out;

import java.util.Optional;

import com.excentria_it.wamya.domain.UserAccount;

public interface LoadUserAccountPort {

	Optional<UserAccount> loadUserAccountByUsername(String username);

}
