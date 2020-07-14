package com.codisiac.wamya.application.port.out;

import com.codisiac.wamya.domain.UserAccount;

public interface CreateUserAccountPort {
	
	void createUserAccount(UserAccount userAccount);

}
