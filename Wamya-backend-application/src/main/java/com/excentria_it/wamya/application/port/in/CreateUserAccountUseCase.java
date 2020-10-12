package com.excentria_it.wamya.application.port.in;

import java.util.Date;
import java.util.Locale;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.excentria_it.wamya.common.exception.UnsupportedInternationalCallingCode;
import com.excentria_it.wamya.common.exception.UserAccountAlreadyExistsException;
import com.excentria_it.wamya.domain.Gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface CreateUserAccountUseCase {

	boolean registerUserAccountCreationDemand(CreateUserAccountCommand command, Locale locale)
			throws UserAccountAlreadyExistsException, UnsupportedInternationalCallingCode;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	class CreateUserAccountCommand {

		@NotNull
		Boolean isTransporter;

		@NotNull
		Gender gender;

		@NotNull
		String firstName;

		@NotNull
		String lastName;

		@NotNull
		Date dateOfBirth;

		@NotNull
		@Pattern(regexp = "^[A-Za-z0-9._-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "{com.excentria_it.wamya.domain.mobilephone.icc.message}")
		String email;

		@NotNull
		@Pattern(regexp = "\\A\\+[0-9]{2,3}\\z", message = "{com.excentria_it.wamya.domain.mobilephone.icc.message}")
		String icc;

		@NotNull
		@Pattern(regexp = "\\A[0-9]{8,10}\\z", message = "{com.excentria_it.wamya.domain.mobilephone.number.message}")
		String mobileNumber;

		@NotNull
		@Pattern(regexp = "^(?=[0-9a-zA-Z@#$%^&+=(?=\\\\S+$)]){8,20}$", message = "{com.excentria_it.wamya.domain.user.password.message}")
		String userPassword;

		@NotNull
		Boolean receiveNewsletter;

	}
}
