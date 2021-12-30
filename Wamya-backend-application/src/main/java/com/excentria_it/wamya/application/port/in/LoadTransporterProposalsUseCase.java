package com.excentria_it.wamya.application.port.in;

import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.excentria_it.wamya.common.PeriodCriterion;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.Period;
import com.excentria_it.wamya.common.annotation.Sort;
import com.excentria_it.wamya.domain.JourneyProposalStatusCode;
import com.excentria_it.wamya.domain.TransporterProposals;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface LoadTransporterProposalsUseCase {

	TransporterProposals loadProposals(LoadTransporterProposalsCommand command, String string);

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	class LoadTransporterProposalsCommand {

		@NotEmpty
		private String transporterUsername;

		@NotNull
		@NotEmpty
		private Set<JourneyProposalStatusCode> statusCodes;

		@NotNull
		private Integer pageNumber;

		@NotNull
		private Integer pageSize;

		@Sort(fields = { "price" })
		private SortCriterion sortingCriterion;

		@Period(value = { "w1", "m1", "lm1", "lm3" })
		private PeriodCriterion periodCriterion;

	}

}
