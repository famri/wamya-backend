package com.excentria_it.wamya.application.service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;

import com.excentria_it.wamya.application.port.in.LoadProposalsUseCase;
import com.excentria_it.wamya.application.port.in.MakeProposalUseCase;
import com.excentria_it.wamya.application.port.in.UpdateProposalUseCase;
import com.excentria_it.wamya.application.port.out.AcceptProposalPort;
import com.excentria_it.wamya.application.port.out.LoadJourneyRequestPort;
import com.excentria_it.wamya.application.port.out.LoadProposalsPort;
import com.excentria_it.wamya.application.port.out.LoadTransporterVehiculesPort;
import com.excentria_it.wamya.application.port.out.MakeProposalPort;
import com.excentria_it.wamya.application.port.out.RejectProposalPort;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.domain.StatusCode;
import com.excentria_it.wamya.common.exception.InvalidTransporterVehiculeException;
import com.excentria_it.wamya.common.exception.JourneyProposalNotFoundException;
import com.excentria_it.wamya.common.exception.JourneyRequestExpiredException;
import com.excentria_it.wamya.common.exception.JourneyRequestNotFoundException;
import com.excentria_it.wamya.domain.ClientJourneyRequestDto;
import com.excentria_it.wamya.domain.JourneyProposalDto.VehiculeDto;
import com.excentria_it.wamya.domain.JourneyRequestInputOutput;
import com.excentria_it.wamya.domain.JourneyRequestProposals;
import com.excentria_it.wamya.domain.LoadJourneyProposalsCriteria;
import com.excentria_it.wamya.domain.MakeProposalDto;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class JourneyProposalService implements MakeProposalUseCase, LoadProposalsUseCase, UpdateProposalUseCase {

	private final MakeProposalPort makeProposalPort;

	private final LoadTransporterVehiculesPort loadTransporterPort;

	private final LoadJourneyRequestPort loadJourneyRequestPort;

	private final LoadProposalsPort loadPropsalsPort;

	private final AcceptProposalPort acceptProposalPort;

	private final RejectProposalPort rejectProposalPort;

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
				.sortingCriterion(command.getSortingCriterion()).statusCodes(command.getStatusCodes()).build();

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

		Optional<JourneyRequestInputOutput> journeyRequestOptional = loadJourneyRequestPort
				.loadJourneyRequestById(journeyRequestId);

		if (journeyRequestOptional.isEmpty()) {
			throw new JourneyRequestNotFoundException("Journey request not found.");
		}

		JourneyRequestInputOutput journeyRequest = journeyRequestOptional.get();
		ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
		if (now.toInstant().isAfter(journeyRequest.getDateTime())) {
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

		if (journeyRequestOptional.isEmpty()) {
			throw new JourneyRequestNotFoundException(String.format("Journey request not found: %d", journeyRequestId));
		}

	}

	@Override
	public void updateProposal(Long journeyRequestId, Long proposalId, StatusCode status, String clientUsername) {

		checkClientJourneyRequestsProposal(clientUsername, journeyRequestId, proposalId);

		if (StatusCode.ACCEPTED.equals(status)) {
			acceptProposalPort.acceptProposal(journeyRequestId, proposalId);
		} else {
			rejectProposalPort.rejectProposal(journeyRequestId, proposalId);
		}

	}

	private void checkClientJourneyRequestsProposal(String clientUsername, Long journeyRequestId, Long proposalId) {

		boolean journeyRequestExists = false;

		if (clientUsername.contains("@")) {

			journeyRequestExists = loadJourneyRequestPort.isExistentAndNotExpiredJourneyRequestByIdAndClientEmail(journeyRequestId,
					clientUsername);
		} else {

			String[] mobileNumber = clientUsername.split("_");

			journeyRequestExists = loadJourneyRequestPort.isExistentAndNotExpiredJourneyRequestByIdAndClientMobileNumberAndIcc(
					journeyRequestId, mobileNumber[1], mobileNumber[0]);
		}

		if (!journeyRequestExists) {
			throw new JourneyRequestNotFoundException(String.format("Journey request not found: %d", journeyRequestId));
		}

		boolean journeyProposalExists = loadPropsalsPort.isExistentJourneyProposalByIdAndJourneyRequestIdAndStatusCode(
				proposalId, journeyRequestId, StatusCode.SUBMITTED);

		if (!journeyProposalExists) {
			throw new JourneyProposalNotFoundException(String.format("Journey proposal not found: %d", proposalId));
		}

	}

}
