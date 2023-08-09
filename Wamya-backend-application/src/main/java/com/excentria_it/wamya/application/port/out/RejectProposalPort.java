package com.excentria_it.wamya.application.port.out;

public interface RejectProposalPort {
	boolean rejectProposal(Long journeyRequestId, Long proposalId);
}
