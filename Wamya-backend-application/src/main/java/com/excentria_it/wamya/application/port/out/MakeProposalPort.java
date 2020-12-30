package com.excentria_it.wamya.application.port.out;

public interface MakeProposalPort {

	Long makeProposal(String username, Double price, Long vehiculeId, Long journeyRequestId);
}
