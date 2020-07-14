package com.codisiac.wamya.application.port.in;

import javax.validation.*;
import javax.validation.constraints.*;

import com.codisiac.wamya.common.*;
import com.codisiac.wamya.domain.UserAccount.*;

import lombok.*;


public interface CreateUserAccountUseCase {

	boolean registerUserAccountCreationDemand(CreateUserAccountCommand command);

	@Value
	@EqualsAndHashCode(callSuper = false)
	class CreateUserAccountCommand extends SelfValidating<CreateUserAccountCommand> {
		@NotNull
		@Valid
		MobilePhoneNumber mobilePhoneNumber;

		@NotNull
		@Valid
		UserPasswordPair userPasswordPair;

		public CreateUserAccountCommand(MobilePhoneNumber mobilePhoneNumber, UserPasswordPair userPasswordPair) {

			this.mobilePhoneNumber = mobilePhoneNumber;
			this.userPasswordPair = userPasswordPair;

			this.validateSelf();
		}

	}
}
