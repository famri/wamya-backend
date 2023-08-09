package com.excentria_it.wamya.application.port.in;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface ValidateMobileValidationCodeUseCase {

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	class ValidateMobileValidationCodeCommand {
		@NotEmpty
		@Size(min = 4, max = 4)
		String code;
	}

	boolean validateCode(ValidateMobileValidationCodeCommand command, String username);
}
