package com.excentria_it.wamya.application.port.in;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.Sort;
import com.excentria_it.wamya.domain.LoadMessagesResult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface LoadMessagesUseCase {

	LoadMessagesResult loadMessages(LoadMessagesCommand command);

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	class LoadMessagesCommand {
		@NotEmpty
		private String username;
		@NotNull
		private Long discussionId;
		@NotNull
		@Min(0)
		private Integer pageNumber;
		@NotNull
		@Min(1)
		private Integer pageSize;

		@Sort(fields = { "date-time" })
		private SortCriterion sortingCriterion;

	}

}
