package com.excentria_it.wamya.application.port.in;

import javax.validation.constraints.NotNull;

import com.excentria_it.wamya.common.annotation.Among;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface AcceptProposalUseCase {

	void acceptProposal(Long journeyRequestId, Long proposalId, String clientUsername, String locale);

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	class AcceptProposalCommand {
		@NotNull
		@Among(value = "accepted")
		private String status;
	}
}
