package com.excentria_it.wamya.application.service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;

import com.excentria_it.wamya.application.port.in.AcceptProposalUseCase;
import com.excentria_it.wamya.application.port.in.LoadProposalsUseCase;
import com.excentria_it.wamya.application.port.in.MakeProposalUseCase;
import com.excentria_it.wamya.application.port.out.AcceptProposalPort;
import com.excentria_it.wamya.application.port.out.LoadJourneyRequestPort;
import com.excentria_it.wamya.application.port.out.LoadProposalsPort;
import com.excentria_it.wamya.application.port.out.LoadTransporterVehiculesPort;
import com.excentria_it.wamya.application.port.out.MakeProposalPort;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.exception.InvalidTransporterVehiculeException;
import com.excentria_it.wamya.common.exception.JourneyProposalNotFoundException;
import com.excentria_it.wamya.common.exception.JourneyRequestExpiredException;
import com.excentria_it.wamya.common.exception.JourneyRequestNotFoundException;
import com.excentria_it.wamya.common.exception.OperationDeniedException;
import com.excentria_it.wamya.domain.ClientJourneyRequestDto;
import com.excentria_it.wamya.domain.CreateJourneyRequestDto;
import com.excentria_it.wamya.domain.JourneyProposalDto;
import com.excentria_it.wamya.domain.JourneyProposalDto.StatusCode;
import com.excentria_it.wamya.domain.JourneyProposalDto.VehiculeDto;
import com.excentria_it.wamya.domain.JourneyRequestProposals;
import com.excentria_it.wamya.domain.LoadJourneyProposalsCriteria;
import com.excentria_it.wamya.domain.MakeProposalDto;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class JourneyProposalService implements MakeProposalUseCase, LoadProposalsUseCase, AcceptProposalUseCase {

	private final MakeProposalPort makeProposalPort;

	private final LoadTransporterVehiculesPort loadTransporterPort;

	private final LoadJourneyRequestPort loadJourneyRequestPort;

	private final LoadProposalsPort loadPropsalsPort;

	private final AcceptProposalPort acceptProposalPort;

	@Override
	public MakeProposalDto makeProposal(MakeProposalCommand command, Long journeyRequestId, String transporterUsername,
			String locale) {

		checkExistentJourneyRequest(journeyRequestId);

		checkTransporterHasVehicule(transporterUsername, command.getVehiculeId());

		MakeProposalDto proposalDto = makeProposalPort.makeProposal(transporterUsername, command.getPrice(),
				command.getVehiculeId(), journeyRequestId, locale);

		return proposalDto;
	}

	@Override
	public JourneyRequestProposals loadProposals(LoadProposalsCommand command, String locale) {

		checkClientJourneyRequest(command.getClientUsername(), command.getJourneyRequestId());

		LoadJourneyProposalsCriteria criteria = LoadJourneyProposalsCriteria.builder()
				.journeyRequestId(command.getJourneyRequestId()).clientUsername(command.getClientUsername())
				.pageNumber(command.getPageNumber()).pageSize(command.getPageSize())
				.sortingCriterion(command.getSortingCriterion()).build();

		return loadPropsalsPort.loadJourneyProposals(criteria, locale);
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
		throw new InvalidTransporterVehiculeException(
				String.format("Invalid vehiculeId for transporter: %d", vehiculeId));
	}

	private void checkExistentJourneyRequest(Long journeyRequestId) {

		Optional<CreateJourneyRequestDto> journeyRequestOptional = loadJourneyRequestPort
				.loadJourneyRequestById(journeyRequestId);

		if (journeyRequestOptional == null || journeyRequestOptional.isEmpty()) {
			throw new JourneyRequestNotFoundException("Journey request not found.");
		}

		CreateJourneyRequestDto journeyRequest = journeyRequestOptional.get();
		ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
		if (now.toInstant().isAfter(journeyRequest.getEndDateTime())) {
			throw new JourneyRequestExpiredException(String.format("Journey request expired: %d", journeyRequestId));
		}

	}

	private void checkClientJourneyRequest(String clientUsername, Long journeyRequestId) {
		Optional<ClientJourneyRequestDto> journeyRequestOptional = null;
		if (clientUsername.contains("@")) {
			journeyRequestOptional = loadJourneyRequestPort.loadJourneyRequestByIdAndClientEmail(journeyRequestId,
					clientUsername);
		} else {

			String[] mobileNumber = clientUsername.split("_");
			journeyRequestOptional = loadJourneyRequestPort.loadJourneyRequestByIdAndClientMobileNumberAndIcc(
					journeyRequestId, mobileNumber[1], mobileNumber[0]);
		}

		if (journeyRequestOptional == null || journeyRequestOptional.isEmpty()) {
			throw new JourneyRequestNotFoundException(String.format("Journey request not found: %d", journeyRequestId));
		}

	}

	@Override
	public void acceptProposal(Long journeyRequestId, Long proposalId, String clientUsername, String locale) {

		checkClientJourneyRequestsProposal(clientUsername, journeyRequestId, proposalId, locale);

		acceptProposalPort.acceptProposal(journeyRequestId, proposalId);

	}

	private void checkClientJourneyRequestsProposal(String clientUsername, Long journeyRequestId, Long proposalId,
			String locale) {

		Optional<ClientJourneyRequestDto> journeyRequestOptional = null;
		if (clientUsername.contains("@")) {
			journeyRequestOptional = loadJourneyRequestPort.loadJourneyRequestByIdAndClientEmail(journeyRequestId,
					clientUsername);
		} else {

			String[] mobileNumber = clientUsername.split("_");
			journeyRequestOptional = loadJourneyRequestPort.loadJourneyRequestByIdAndClientMobileNumberAndIcc(
					journeyRequestId, mobileNumber[1], mobileNumber[0]);
		}

		if (journeyRequestOptional == null || journeyRequestOptional.isEmpty()) {
			throw new JourneyRequestNotFoundException(String.format("Journey request not found: %d", journeyRequestId));
		}

		Optional<JourneyProposalDto> journeyProposalDto = loadPropsalsPort
				.loadJourneyProposalByIdAndJourneyRequestId(proposalId, journeyRequestId, locale);

		if (journeyProposalDto == null || journeyProposalDto.isEmpty()) {
			throw new JourneyProposalNotFoundException(String.format("Journey proposal not found: %d", proposalId));
		}

		if (journeyProposalDto.get().getStatus() == null
				|| !StatusCode.SUBMITTED.equals(journeyProposalDto.get().getStatus().getCode())) {
			throw new OperationDeniedException(String.format("Journey proposal %d could not be modified.", proposalId));
		}

	}

}
