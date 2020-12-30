package com.excentria_it.wamya.test.data.common;

import com.excentria_it.wamya.application.port.in.MakeProposalUseCase.MakeProposalCommand;
import com.excentria_it.wamya.application.port.in.MakeProposalUseCase.MakeProposalCommand.MakeProposalCommandBuilder;;

public class JourneyProposalTestData {

	public static final Double JOURNEY_PROPOSAL_PRICE = 250.0;

	public static final MakeProposalCommandBuilder defaultMakeProposalCommandBuilder() {
		return MakeProposalCommand.builder().price(JOURNEY_PROPOSAL_PRICE).vehiculeId(1L);
	}
}
