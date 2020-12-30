package com.excentria_it.wamya.application.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;

import com.excentria_it.wamya.application.port.in.MakeProposalUseCase;
import com.excentria_it.wamya.application.port.out.LoadJourneyRequestPort;
import com.excentria_it.wamya.application.port.out.LoadTransporterPort;
import com.excentria_it.wamya.application.port.out.MakeProposalPort;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.exception.InvalidTransporterVehiculeException;
import com.excentria_it.wamya.common.exception.JourneyRequestExpiredException;
import com.excentria_it.wamya.common.exception.JourneyRequestNotFoundException;
import com.excentria_it.wamya.domain.CreateJourneyRequestDto;
import com.excentria_it.wamya.domain.JourneyProposalDto.VehiculeDto;
import com.excentria_it.wamya.domain.MakeProposalDto;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class MakeProposalService implements MakeProposalUseCase {

	private final MakeProposalPort makeProposalPort;

	private final LoadTransporterPort loadTransporterPort;

	private final LoadJourneyRequestPort loadJourneyRequestPort;

	@Override
	public MakeProposalDto makeProposal(MakeProposalCommand command, Long journeyRequestId,
			String transporterUsername) {

		checkExistentJourneyRequest(journeyRequestId);

		checkTransporterHasVehicule(transporterUsername, command.getVehiculeId());

		Long proposalId = makeProposalPort.makeProposal(transporterUsername, command.getPrice(),
				command.getVehiculeId(), journeyRequestId);

		return new MakeProposalDto(proposalId, command.getPrice());
	}

	private void checkTransporterHasVehicule(String transporterUsername, Long vehiculeId) {
		Set<VehiculeDto> vehicules = null;
		if (transporterUsername.contains("@")) {
			vehicules = loadTransporterPort.loadTransporterVehicules(transporterUsername);
		} else {

			String[] mobileNumber = transporterUsername.split("_");

			vehicules = loadTransporterPort.loadTransporterVehicules(mobileNumber[0], mobileNumber[1]);
		}

		if (!CollectionUtils.isEmpty(vehicules) && vehicules.stream().anyMatch(v -> v.getId().equals(vehiculeId))) {
			return;
		}
		throw new InvalidTransporterVehiculeException("Invalid vehiculeId for transporter.");
	}

	private void checkExistentJourneyRequest(Long journeyRequestId) {

		Optional<CreateJourneyRequestDto> journeyRequestOptional = loadJourneyRequestPort
				.loadJourneyRequestById(journeyRequestId);

		if (journeyRequestOptional == null || journeyRequestOptional.isEmpty()) {
			throw new JourneyRequestNotFoundException("Journey request not found.");
		}

		CreateJourneyRequestDto journeyRequest = journeyRequestOptional.get();
		LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
		if (now.isAfter(journeyRequest.getEndDateTime())) {
			throw new JourneyRequestExpiredException("Journey request expired.");
		}

	}
}
