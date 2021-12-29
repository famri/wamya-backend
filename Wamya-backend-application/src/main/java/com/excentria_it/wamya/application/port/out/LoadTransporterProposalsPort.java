package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.domain.LoadTransporterProposalsCriteria;
import com.excentria_it.wamya.domain.TransporterProposalsOutput;

public interface LoadTransporterProposalsPort {
	
	TransporterProposalsOutput loadTransporterProposals(LoadTransporterProposalsCriteria criteria, String locale);
}
