package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.domain.UserAccount;

public interface CreateUserAccountPort {
	
	void createUserAccount(UserAccount userAccount);

}
