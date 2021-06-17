package com.excentria_it.wamya.application.port.in;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface UpdateDeviceRegistrationTokenUseCase {
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	class UpdateDeviceRegistrationTokenCommand {

		@NotEmpty
		String token;

	}

	void updateToken(String token, String username);

}
