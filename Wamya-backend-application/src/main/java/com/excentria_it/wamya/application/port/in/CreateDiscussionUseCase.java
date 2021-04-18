package com.excentria_it.wamya.application.port.in;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.excentria_it.wamya.domain.LoadDiscussionsDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface CreateDiscussionUseCase {

	LoadDiscussionsDto createDiscussion(CreateDiscussionCommand command, String username);

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	class CreateDiscussionCommand {

		@NotNull
		private Long clientId;
		@NotNull
		private Long transporterId;
	
	}

}
