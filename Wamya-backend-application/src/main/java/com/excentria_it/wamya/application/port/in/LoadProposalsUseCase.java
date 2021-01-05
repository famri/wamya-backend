package com.excentria_it.wamya.application.port.in;

import javax.validation.constraints.NotNull;

import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.Sort;
import com.excentria_it.wamya.domain.JourneyRequestProposals;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface LoadProposalsUseCase {

	JourneyRequestProposals loadProposals(LoadProposalsCommand command, String locale);

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	class LoadProposalsCommand {

		@NotNull
		private Integer pageNumber;

		@NotNull
		private Integer pageSize;

		@Sort(fields = { "price" })
		private SortCriterion sortingCriterion;

		@NotNull
		private Long journeyRequestId;

		@NotNull
		private String clientUsername;
	}
}
