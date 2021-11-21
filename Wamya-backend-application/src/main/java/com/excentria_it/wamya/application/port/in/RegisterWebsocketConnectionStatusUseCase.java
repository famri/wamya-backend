package com.excentria_it.wamya.application.port.in;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface RegisterWebsocketConnectionStatusUseCase {

	void registerConnectionStatus(RegisterConnectionStatusCommand command);

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	class RegisterConnectionStatusCommand {
		@NotNull
		private String username;

		@NotNull
		private Boolean connected;
	}
}
