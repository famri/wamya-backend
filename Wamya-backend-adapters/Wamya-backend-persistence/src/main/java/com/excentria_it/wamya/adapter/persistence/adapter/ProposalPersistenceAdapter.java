package com.excentria_it.wamya.adapter.persistence.adapter;

import com.excentria_it.wamya.adapter.persistence.entity.*;
import com.excentria_it.wamya.adapter.persistence.mapper.JourneyProposalMapper;
import com.excentria_it.wamya.adapter.persistence.repository.*;
import com.excentria_it.wamya.application.port.out.*;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.common.domain.StatusCode;
import com.excentria_it.wamya.common.utils.ParameterUtils;
import com.excentria_it.wamya.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@PersistenceAdapter
public class ProposalPersistenceAdapter implements MakeProposalPort, LoadProposalsPort, AcceptProposalPort,
        RejectProposalPort, LoadTransporterProposalsPort {

    private final JourneyProposalRepository journeyProposalRepository;

    private final JourneyRequestRepository journeyRequestRepository;

    private final TransporterRepository transporterRepository;

    private final VehicleRepository vehicleRepository;

    private final JourneyProposalMapper journeyProposalMapper;

    private final JourneyProposalStatusRepository journeyProposalStatusRepository;

    @Override
    public MakeProposalDto makeProposal(String transporterSubject, Double price, Long vehicleId, Long journeyRequestId,
                                        String locale) {

        Optional<TransporterJpaEntity> transporterJpaEntityOptional = transporterRepository.findTransporterBySubject(transporterSubject);

        Optional<VehicleJpaEntity> vehicleJpaEntityOptional = vehicleRepository.findById(vehicleId);

        JourneyProposalJpaEntity journeyProposalJpaEntity = journeyProposalMapper.mapToJpaEntity(price,
                transporterJpaEntityOptional.get(), vehicleJpaEntityOptional.get());

        JourneyProposalStatusJpaEntity status = journeyProposalStatusRepository
                .findByCode(JourneyProposalStatusCode.SUBMITTED);
        journeyProposalJpaEntity.setStatus(status);

        journeyProposalJpaEntity = journeyProposalRepository.save(journeyProposalJpaEntity);

        Optional<JourneyRequestJpaEntity> journeyRequestJpaEntityOptional = journeyRequestRepository
                .findById(journeyRequestId);

        JourneyRequestJpaEntity journeyRequestJpaEntity = journeyRequestJpaEntityOptional.get();
        journeyRequestJpaEntity.addProposal(journeyProposalJpaEntity);

        journeyRequestRepository.save(journeyRequestJpaEntity);

        return new MakeProposalDto(journeyProposalJpaEntity.getId(), price, status.getValue(locale));
    }

    @Override
    public JourneyRequestProposals loadJourneyProposals(LoadJourneyProposalsCriteria criteria, String locale) {
        Sort sort = convertToSort(criteria.getSortingCriterion());
        List<JourneyProposalJpaEntity> list;

        if (criteria.getStatusCodes().isEmpty()) {
            list = journeyProposalRepository.findByJourneyRequest_Id(criteria.getJourneyRequestId(), sort);
        } else {
            List<JourneyProposalStatusCode> statusCodes = criteria.getStatusCodes().stream()
                    .map(s -> JourneyProposalStatusCode.valueOf(s.name())).collect(Collectors.toList());
            list = journeyProposalRepository.findByJourneyRequest_IdAndStatus_CodeIn(criteria.getJourneyRequestId(),
                    statusCodes, sort);
        }

        if (list == null) {
            return new JourneyRequestProposals(0, Collections.<JourneyProposalDto>emptyList());
        }

        List<JourneyProposalDto> journeyProposalDtoList = list.stream()
                .map(p -> journeyProposalMapper.mapToJourneyProposalDto(p, locale)).collect(Collectors.toList());

        return new JourneyRequestProposals(list.size(), journeyProposalDtoList);

    }

    @Override
    public Optional<JourneyProposalDto> loadJourneyProposalByIdAndJourneyRequestId(Long proposalId,
                                                                                   Long journeyRequestId, String locale) {

        Optional<JourneyProposalJpaEntity> journeyProposalJpaEntity = journeyProposalRepository
                .findByIdAndJourneyRequest_Id(proposalId, journeyRequestId);

        if (journeyProposalJpaEntity.isEmpty())
            return Optional.empty();

        return Optional.of(journeyProposalMapper.mapToJourneyProposalDto(journeyProposalJpaEntity.get(), locale));
    }

    @Override
    public boolean acceptProposal(Long journeyRequestId, Long proposalId) {

        Optional<JourneyRequestJpaEntity> journeyRequestOptional = journeyRequestRepository.findById(journeyRequestId);

        if (journeyRequestOptional.isEmpty())
            return false;

        Set<JourneyProposalJpaEntity> journeyProposals = journeyRequestOptional.get().getProposals();
        boolean proposalExists = journeyProposals.stream().anyMatch(p -> p.getId().equals(proposalId));

        if (!proposalExists)
            return false;

        JourneyProposalStatusJpaEntity acceptedStatus = journeyProposalStatusRepository
                .findByCode(JourneyProposalStatusCode.ACCEPTED);

        JourneyProposalStatusJpaEntity rejectedStatus = journeyProposalStatusRepository
                .findByCode(JourneyProposalStatusCode.REJECTED);

        journeyProposals.forEach(p -> {
            if (p.getId().equals(proposalId)) {
                p.setStatus(acceptedStatus);
            } else {
                p.setStatus(rejectedStatus);
            }
        });

        journeyRequestRepository.save(journeyRequestOptional.get());
        return true;
    }

    @Override
    public boolean rejectProposal(Long journeyRequestId, Long proposalId) {
        Optional<JourneyRequestJpaEntity> journeyRequestOptional = journeyRequestRepository.findById(journeyRequestId);

        if (journeyRequestOptional.isEmpty())
            return false;

        Set<JourneyProposalJpaEntity> journeyProposals = journeyRequestOptional.get().getProposals();
        boolean proposalExists = journeyProposals.stream().anyMatch(p -> p.getId().equals(proposalId));

        if (!proposalExists)
            return false;

        JourneyProposalStatusJpaEntity rejectedStatus = journeyProposalStatusRepository
                .findByCode(JourneyProposalStatusCode.REJECTED);

        journeyProposals.stream().filter(p -> p.getId().equals(proposalId)).forEach(p -> p.setStatus(rejectedStatus));

        journeyRequestRepository.save(journeyRequestOptional.get());

        return true;
    }

    private Sort convertToSort(SortCriterion sortingCriterion) {

        return Sort.by(Direction.valueOf(sortingCriterion.getDirection().toUpperCase()),
                ParameterUtils.kebabToCamelCase(sortingCriterion.getField()));
    }

    @Override
    public boolean isExistentJourneyProposalByIdAndJourneyRequestIdAndStatusCode(Long proposalId, Long journeyRequestId,
                                                                                 StatusCode statusCode) {

        return journeyProposalRepository.existsByIdAndJourneyRequestIdAndStatusCode(proposalId, journeyRequestId,
                JourneyProposalStatusCode.valueOf(statusCode.name()));

    }

    @Override
    public Set<TransporterNotificationInfo> loadTransportersNotificationInfo(Long journeyRequestId) {
        return journeyProposalRepository.loadTransportersNotificationInfo(journeyRequestId);

    }

    @Override
    public TransporterProposalsOutput loadTransporterProposals(LoadTransporterProposalsCriteria criteria, String locale) {
        Sort sort = convertToSort(criteria.getSortingCriterion());

        Pageable pagingSort = PageRequest.of(criteria.getPageNumber(), criteria.getPageSize(), sort);

        Page<JourneyProposalJpaEntity> transporterProposalsPage = journeyProposalRepository
                .findByTransporter_EmailAndJourneyDateTimeBetweenAndProposal_Status_Code(
                        criteria.getPeriodCriterion().getLowerEdge().toInstant(),
                        criteria.getPeriodCriterion().getHigherEdge().toInstant(), criteria.getTransporterUsername(),
                        criteria.getStatusCodes(), locale, pagingSort);

        return TransporterProposalsOutput.builder().totalPages(transporterProposalsPage.getTotalPages())
                .totalElements(transporterProposalsPage.getTotalElements())
                .pageNumber(transporterProposalsPage.getNumber()).pageSize(transporterProposalsPage.getSize())
                .hasNext(transporterProposalsPage.hasNext())
                .content(transporterProposalsPage.getContent().stream()
                        .map(tp -> journeyProposalMapper.mapToTransporterProposalOutput(tp, locale))
                        .collect(Collectors.toList()))
                .build();

    }
}
