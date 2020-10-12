package com.excentria_it.wamya.application.port.in;

import java.util.Locale;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface SendValidationCodeUseCase {

	boolean sendSMSValidationCode(SendSMSValidationCodeCommand command, Locale locale);

	boolean sendEmailValidationCode(SendEmailValidationCodeCommand command, Locale locale);

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	class SendSMSValidationCodeCommand {
		@NotNull
		@Pattern(regexp = "\\A[0-9]{8,10}\\z", message = "{com.excentria_it.wamya.domain.mobilephone.number.message}")
		String mobileNumber;

		@NotNull
		@Pattern(regexp = "\\A\\+[0-9]{2,3}\\z", message = "{com.excentria_it.wamya.domain.mobilephone.icc.message}")
		String icc;

	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	class SendEmailValidationCodeCommand {

		@NotNull
		@Pattern(regexp = "^[A-Za-z0-9._-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "{com.excentria_it.wamya.domain.mobilephone.icc.message}")
		String email;

	}
}
