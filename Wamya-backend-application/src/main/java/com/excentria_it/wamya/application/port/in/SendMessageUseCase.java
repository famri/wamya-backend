package com.excentria_it.wamya.application.port.in;

import javax.validation.constraints.NotEmpty;

import com.excentria_it.wamya.domain.LoadDiscussionsDto.MessageDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface SendMessageUseCase {

	MessageDto sendMessage(SendMessageCommand command, Long discussionId, String senderUsername);

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder

	class SendMessageCommand {
		@NotEmpty
		private String content;

	}
}
