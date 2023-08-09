package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.domain.MakeProposalDto;

public interface MakeProposalPort {

	MakeProposalDto makeProposal(String username, Double price, Long vehiculeId, Long journeyRequestId, String locale);
}
