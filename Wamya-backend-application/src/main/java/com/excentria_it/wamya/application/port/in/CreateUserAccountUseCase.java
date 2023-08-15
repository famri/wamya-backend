package com.excentria_it.wamya.application.port.in;

import java.time.LocalDate;
import java.util.Locale;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.excentria_it.wamya.common.exception.UnsupportedInternationalCallingCodeException;
import com.excentria_it.wamya.common.exception.UserAccountAlreadyExistsException;
import com.excentria_it.wamya.domain.OpenIdAuthResponse;
import com.excentria_it.wamya.domain.RegexPattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface CreateUserAccountUseCase {

	OpenIdAuthResponse registerUserAccountCreationDemand(CreateUserAccountCommand command, Locale locale)
			throws UserAccountAlreadyExistsException, UnsupportedInternationalCallingCodeException;

	void checkExistingAccount(CreateUserAccountCommand command);

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	class CreateUserAccountCommand {

		Long id;

		@NotNull
		Boolean isTransporter;

		@NotNull
		Long genderId;

		@NotNull
		String firstname;

		@NotNull
		String lastname;

		@NotNull
		LocalDate dateOfBirth;

		@NotNull
		@Pattern(regexp = RegexPattern.EMAIL_PATTERN, message = "{com.excentria_it.wamya.domain.user.email.message}")
		String email;

		@NotNull
		@Pattern(regexp = RegexPattern.ICC_PATTERN, message = "{com.excentria_it.wamya.domain.mobilephone.icc.message}")
		String icc;

		@NotNull
		@Pattern(regexp = RegexPattern.FRENCH_MOBILE_NUMBER_PATTERN, message = "{com.excentria_it.wamya.domain.mobilephone.number.message}")
		String mobileNumber;

		@NotNull
		@Pattern(regexp = RegexPattern.USER_PASSWORD_PATTERN, message = "{com.excentria_it.wamya.domain.user.password.message}")
		String userPassword;

		@NotNull
		Boolean receiveNewsletter;

	}
}
