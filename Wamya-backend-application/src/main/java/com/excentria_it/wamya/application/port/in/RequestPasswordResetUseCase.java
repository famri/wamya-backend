package com.excentria_it.wamya.application.port.in;

import java.util.Locale;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface RequestPasswordResetUseCase {

	void requestPasswordReset(String username, Locale locale);

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	class RequestPasswordResetCommand {

		@NotEmpty
		String username;

	}
}
