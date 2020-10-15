package com.excentria_it.wamya.application.port.in;

import java.util.Date;
import java.util.Locale;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.excentria_it.wamya.common.exception.UnsupportedInternationalCallingCode;
import com.excentria_it.wamya.common.exception.UserAccountAlreadyExistsException;
import com.excentria_it.wamya.domain.Gender;
import com.excentria_it.wamya.domain.RegexPattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface CreateUserAccountUseCase {

	Long registerUserAccountCreationDemand(CreateUserAccountCommand command, Locale locale)
			throws UserAccountAlreadyExistsException, UnsupportedInternationalCallingCode;

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
		Gender gender;

		@NotNull
		String firstName;

		@NotNull
		String lastName;

		@NotNull
		Date dateOfBirth;

		@NotNull
		@Pattern(regexp = RegexPattern.EMAIL_PATTERN, message = "{com.excentria_it.wamya.domain.mobilephone.icc.message}")
		String email;

		@NotNull
		@Pattern(regexp = RegexPattern.ICC_PATTERN, message = "{com.excentria_it.wamya.domain.mobilephone.icc.message}")
		String icc;

		@NotNull
		@Pattern(regexp = RegexPattern.MOBILE_NUMBER_PATTERN, message = "{com.excentria_it.wamya.domain.mobilephone.number.message}")
		String mobileNumber;

		@NotNull
		@Pattern(regexp = RegexPattern.USER_PASSWORD_PATTERN, message = "{com.excentria_it.wamya.domain.user.password.message}")
		String userPassword;

		@NotNull
		Boolean receiveNewsletter;

	}
}
