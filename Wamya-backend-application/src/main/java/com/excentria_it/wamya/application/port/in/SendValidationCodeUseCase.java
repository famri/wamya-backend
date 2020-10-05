package com.excentria_it.wamya.application.port.in;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.excentria_it.wamya.domain.UserAccount.MobilePhoneNumber;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface SendValidationCodeUseCase {

	boolean sendValidationCode(SendValidationCodeCommand command);

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	class SendValidationCodeCommand {
		@NotNull
		@Valid
		MobilePhoneNumber mobilePhoneNumber;
		
	}
}
