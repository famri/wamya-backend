package com.excentria_it.wamya.application.port.out;

public interface AcceptProposalPort {

	boolean acceptProposal(Long journeyRequestId, Long proposalId);

}
