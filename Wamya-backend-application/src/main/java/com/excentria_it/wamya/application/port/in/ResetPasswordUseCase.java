package com.excentria_it.wamya.application.port.in;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface ResetPasswordUseCase {

	boolean checkRequest(String uuid, Long expiry);

	boolean resetPassword(String uuid, String password);

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	class ResetPasswordCommand {

		@NotEmpty
		String password;

		@NotEmpty
		String uuid;

		@NotNull
		Long expiry;

	}

}
