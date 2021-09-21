package com.excentria_it.wamya.application.port.in;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.excentria_it.wamya.domain.LoadDiscussionsDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface FindDiscussionUseCase {

	LoadDiscussionsDto findDiscussionById(FindDiscussionByIdCommand command);

	LoadDiscussionsDto findDiscussionByClientIdAndTransporterId(
			FindDiscussionByClientIdAndTransporterIdCommand command);

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	class FindDiscussionByIdCommand {
		@NotEmpty
		private String username;
		@NotNull
		private Long discussionId;

	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	class FindDiscussionByClientIdAndTransporterIdCommand {
		@NotEmpty
		private String username;
		@NotNull
		private Long clientId;
		@NotNull
		private Long transporterId;
	}

}
