package com.excentria_it.wamya.test.data.common;

import java.util.List;

import com.excentria_it.wamya.application.port.in.LoadProposalsUseCase.LoadProposalsCommand;
import com.excentria_it.wamya.application.port.in.LoadProposalsUseCase.LoadProposalsCommand.LoadProposalsCommandBuilder;
import com.excentria_it.wamya.application.port.in.MakeProposalUseCase.MakeProposalCommand;
import com.excentria_it.wamya.application.port.in.MakeProposalUseCase.MakeProposalCommand.MakeProposalCommandBuilder;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.domain.JourneyProposalDto;
import com.excentria_it.wamya.domain.JourneyProposalDto.TransporterDto;
import com.excentria_it.wamya.domain.JourneyProposalDto.VehiculeDto;
import com.excentria_it.wamya.domain.JourneyRequestProposals;
import com.excentria_it.wamya.domain.LoadJourneyProposalsCriteria;
import com.excentria_it.wamya.domain.LoadJourneyProposalsCriteria.LoadJourneyProposalsCriteriaBuilder;

public class JourneyProposalTestData {
	private static final List<JourneyProposalDto> journeyProposalDtos = List.of(
			JourneyProposalDto.builder().id(1L).price(220.0)
					.transporterDto(new TransporterDto(1L, "transporter1", "https://path/to/transporter1/photo", 4.5))
					.vehiculeDto(new VehiculeDto(1L, "RENAULT", "KANGOO", "https://path/to/vehicule1/photo")).build(),
			JourneyProposalDto.builder().id(2L).price(230.0)
					.transporterDto(new TransporterDto(2L, "transporter2", "https://path/to/transporter2/photo", 4.9))
					.vehiculeDto(new VehiculeDto(2L, "PEUGEOT", "PARTNER", "https://path/to/vehicule2/photo")).build(),
			JourneyProposalDto.builder().id(2L).price(240.0)
					.transporterDto(new TransporterDto(3L, "transporter3", "https://path/to/transporter3/photo", 4.7))
					.vehiculeDto(new VehiculeDto(3L, "PEUGEOT", "PARTNER", "https://path/to/vehicule3/photo")).build());

	public static final Double JOURNEY_PROPOSAL_PRICE = 250.0;

	public static final MakeProposalCommandBuilder defaultMakeProposalCommandBuilder() {
		return MakeProposalCommand.builder().price(JOURNEY_PROPOSAL_PRICE).vehiculeId(1L);
	}

	public static final LoadProposalsCommandBuilder defaultLoadProposalsCommandBuilder() {
		return LoadProposalsCommand.builder().clientUsername(TestConstants.DEFAULT_EMAIL).journeyRequestId(1L)
				.pageNumber(0).pageSize(25).sortingCriterion(new SortCriterion("price", "asc"));
	}

	public static final JourneyRequestProposals defaultJourneyRequestProposals() {
		return new JourneyRequestProposals(1, 3, 0, 25, false, journeyProposalDtos);
	}

	public static final JourneyProposalDto defaultJourneyProposalDto() {
		return journeyProposalDtos.get(0);
	}

	public static final LoadJourneyProposalsCriteriaBuilder defaultLoadJourneyProposalsCriteriaBuilder() {
		return LoadJourneyProposalsCriteria.builder().clientUsername(TestConstants.DEFAULT_EMAIL).journeyRequestId(1L)
				.pageNumber(0).pageSize(25).sortingCriterion(new SortCriterion("price", "asc"));
	}
}
