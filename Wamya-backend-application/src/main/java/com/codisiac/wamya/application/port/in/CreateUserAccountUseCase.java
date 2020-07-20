package com.codisiac.wamya.application.port.in;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.codisiac.wamya.domain.UserAccount.MobilePhoneNumber;
import com.codisiac.wamya.domain.UserAccount.UserPasswordPair;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface CreateUserAccountUseCase {

	boolean registerUserAccountCreationDemand(CreateUserAccountCommand command);

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	class CreateUserAccountCommand {
		@NotNull
		@Valid
		MobilePhoneNumber mobilePhoneNumber;

		@NotNull
		@Valid
		UserPasswordPair userPasswordPair;

	}
}
