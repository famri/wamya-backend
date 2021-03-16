package com.excentria_it.wamya.application.port.in;

import javax.validation.constraints.NotNull;

import com.excentria_it.wamya.common.annotation.Among;
import com.excentria_it.wamya.domain.JourneyProposalDto.StatusCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface UpdateProposalUseCase {

	void updateProposal(Long journeyRequestId, Long proposalId, StatusCode status, String clientUsername);

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	class UpdateProposalCommand {
		@NotNull
		@Among(value = { "accepted", "rejected" })
		private String status;
	}
}
