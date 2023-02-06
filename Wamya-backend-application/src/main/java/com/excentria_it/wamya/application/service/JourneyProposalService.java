package com.excentria_it.wamya.application.service;

import com.excentria_it.wamya.application.port.in.LoadProposalsUseCase;
import com.excentria_it.wamya.application.port.in.MakeProposalUseCase;
import com.excentria_it.wamya.application.port.in.UpdateProposalUseCase;
import com.excentria_it.wamya.application.port.out.*;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.domain.StatusCode;
import com.excentria_it.wamya.common.exception.InvalidTransporterVehicleException;
import com.excentria_it.wamya.common.exception.JourneyProposalNotFoundException;
import com.excentria_it.wamya.common.exception.JourneyRequestExpiredException;
import com.excentria_it.wamya.common.exception.JourneyRequestNotFoundException;
import com.excentria_it.wamya.domain.*;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

@UseCase
@Transactional
@RequiredArgsConstructor
public class JourneyProposalService implements MakeProposalUseCase, LoadProposalsUseCase, UpdateProposalUseCase {

    private final MakeProposalPort makeProposalPort;

    private final CheckUserVehiclePort checkUserVehiclePort;

    private final LoadJourneyRequestPort loadJourneyRequestPort;

    private final LoadProposalsPort loadProposalsPort;

    private final AcceptProposalPort acceptProposalPort;

    private final RejectProposalPort rejectProposalPort;

    @Override
    public MakeProposalDto makeProposal(MakeProposalCommand command, Long journeyRequestId, String transporterSubject,
                                        String locale) {

        checkExistentJourneyRequest(journeyRequestId);

        checkTransporterHasVehicle(transporterSubject, command.getVehicleId());

        MakeProposalDto proposalDto = makeProposalPort.makeProposal(transporterSubject, command.getPrice(),
                command.getVehicleId(), journeyRequestId, locale);

        return proposalDto;
    }

    @Override
    public JourneyRequestProposals loadProposals(LoadProposalsCommand command, String locale) {

        checkClientJourneyRequest(command.getClientUsername(), command.getJourneyRequestId(), locale);

        LoadJourneyProposalsCriteria criteria = LoadJourneyProposalsCriteria.builder()
                .journeyRequestId(command.getJourneyRequestId()).clientUsername(command.getClientUsername())

                .sortingCriterion(command.getSortingCriterion()).statusCodes(command.getStatusCodes()).build();

        return loadProposalsPort.loadJourneyProposals(criteria, locale);
    }

    private void checkTransporterHasVehicle(String transporterUsername, Long vehicleId) {

        if (!checkUserVehiclePort.isUserVehicle(transporterUsername, vehicleId)) {
            throw new InvalidTransporterVehicleException(
                    String.format("Invalid vehicleId for transporter: %d", vehicleId));
        }

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

    private void checkClientJourneyRequest(String clientUsername, Long journeyRequestId, String locale) {
        Optional<ClientJourneyRequestDtoOutput> journeyRequestOptional = null;
        journeyRequestOptional = loadJourneyRequestPort.loadJourneyRequestByIdAndClientSubject(journeyRequestId,
                clientUsername, locale);

        if (journeyRequestOptional.isEmpty()) {
            throw new JourneyRequestNotFoundException(String.format("Journey request not found: %d", journeyRequestId));
        }

    }

    @Override
    public void updateProposal(Long journeyRequestId, Long proposalId, StatusCode status, String subject) {

        checkClientJourneyRequestsProposal(subject, journeyRequestId, proposalId);

        if (StatusCode.ACCEPTED.equals(status)) {
            acceptProposalPort.acceptProposal(journeyRequestId, proposalId);
        } else {
            rejectProposalPort.rejectProposal(journeyRequestId, proposalId);
        }

    }

    private void checkClientJourneyRequestsProposal(String subject, Long journeyRequestId, Long proposalId) {

        boolean journeyRequestExists = loadJourneyRequestPort
                .isExistentAndNotExpiredJourneyRequestByIdAndClientSubject(journeyRequestId, subject);


        if (!journeyRequestExists) {
            throw new JourneyRequestNotFoundException(String.format("Journey request not found: %d", journeyRequestId));
        }

        boolean journeyProposalExists = loadProposalsPort.isExistentJourneyProposalByIdAndJourneyRequestIdAndStatusCode(
                proposalId, journeyRequestId, StatusCode.SUBMITTED);

        if (!journeyProposalExists) {
            throw new JourneyProposalNotFoundException(String.format("Journey proposal not found: %d", proposalId));
        }

    }

}
