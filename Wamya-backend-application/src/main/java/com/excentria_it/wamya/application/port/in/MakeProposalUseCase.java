package com.excentria_it.wamya.application.port.in;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.excentria_it.wamya.domain.MakeProposalDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface MakeProposalUseCase {

	MakeProposalDto makeProposal(MakeProposalCommand command, Long journeyRequestId, String transporterUsername);

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	class MakeProposalCommand {
		@Min(0)
		@NotNull
		private Double price;

		@NotNull
		private Long vehiculeId;
	}
}
