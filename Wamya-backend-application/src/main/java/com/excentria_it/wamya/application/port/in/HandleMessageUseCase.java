package com.excentria_it.wamya.application.port.in;

import javax.validation.constraints.NotEmpty;

import com.excentria_it.wamya.domain.ChatMessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface HandleMessageUseCase {

	ChatMessage handleMessage(HandleMessageCommand command);

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	class HandleMessageCommand {

		@NotEmpty
		private String content;

		@NotEmpty
		private Long senderId;

		@NotEmpty
		private Long receiverId;
	}
}
