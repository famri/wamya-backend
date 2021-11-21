package com.excentria_it.wamya.application.port.in;

import javax.validation.constraints.NotEmpty;

import com.excentria_it.wamya.common.annotation.Among;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface UpdateMessageReadStatusUseCase {

	void updateMessageReadStatus(Long discussionId, Long messageId, String username, UpdateMessageReadStatusCommand command);

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	class UpdateMessageReadStatusCommand {

		@NotEmpty
		@Among(value = { "true" })
		private String isRead;
	}

}
