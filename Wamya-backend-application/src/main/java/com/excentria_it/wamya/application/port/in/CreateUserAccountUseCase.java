package com.excentria_it.wamya.application.port.in;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.excentria_it.wamya.domain.UserAccount.MobilePhoneNumber;
import com.excentria_it.wamya.domain.UserAccount.UserPasswordPair;

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
