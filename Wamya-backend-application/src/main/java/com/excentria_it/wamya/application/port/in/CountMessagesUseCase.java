package com.excentria_it.wamya.application.port.in;

import javax.validation.constraints.NotEmpty;

import com.excentria_it.wamya.common.annotation.Among;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface CountMessagesUseCase {

	Long countMessages(CountMessagesCommand command);

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	class CountMessagesCommand {
		@NotEmpty
		String subject;

		@Among(value = { "false" })
		String read;
	}
}
