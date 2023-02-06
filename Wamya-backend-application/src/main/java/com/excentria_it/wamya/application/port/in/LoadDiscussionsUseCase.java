package com.excentria_it.wamya.application.port.in;

import javax.validation.constraints.NotNull;

import com.excentria_it.wamya.common.FilterCriterion;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.Filter;
import com.excentria_it.wamya.common.annotation.Sort;
import com.excentria_it.wamya.domain.LoadDiscussionsResult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface LoadDiscussionsUseCase {

	LoadDiscussionsResult loadDiscussions(LoadDiscussionsCommand command);

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	class LoadDiscussionsCommand {
		@NotNull
		private String subject;
		@NotNull
		private Integer pageNumber;
		@NotNull
		private Integer pageSize;

		@Sort(fields = { "date-time" })
		private SortCriterion sortingCriterion;

		@Filter(fields = { "active" }, values = { "true,false" })
		private FilterCriterion filteringCriterion;
	}

}
