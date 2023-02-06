package com.excentria_it.wamya.adapter.persistence.adapter;

import com.excentria_it.wamya.adapter.persistence.entity.*;
import com.excentria_it.wamya.adapter.persistence.mapper.JourneyRequestMapper;
import com.excentria_it.wamya.adapter.persistence.mapper.PlaceMapper;
import com.excentria_it.wamya.adapter.persistence.repository.*;
import com.excentria_it.wamya.adapter.persistence.utils.DepartmentJpaEntityResolver;
import com.excentria_it.wamya.adapter.persistence.utils.LocalizedPlaceJpaEntityResolver;
import com.excentria_it.wamya.application.port.out.*;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.common.utils.LocaleUtils;
import com.excentria_it.wamya.common.utils.ParameterUtils;
import com.excentria_it.wamya.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@PersistenceAdapter
@Slf4j
public class JourneyRequestsPersistenceAdapter implements SearchJourneyRequestsPort, CreateJourneyRequestPort,
        LoadJourneyRequestPort, LoadClientJourneyRequestsPort, UpdateJourneyRequestPort, CancelJourneyRequestPort {

    private final JourneyRequestRepository journeyRequestRepository;

    private final EngineTypeRepository engineTypeRepository;

    private final ClientRepository clientRepository;

    private final PlaceRepository placeRepository;

    private final JourneyRequestMapper journeyRequestMapper;

    private final PlaceMapper placeMapper;

    private final JourneyRequestStatusRepository journeyRequestStatusRepository;

    private final DepartmentJpaEntityResolver departmentResolver;

    private final LocalizedPlaceJpaEntityResolver localizedPlaceResolver;

    @Override
    public JourneyRequestsSearchOutputResult searchJourneyRequests(SearchJourneyRequestsInput command) {

        Sort sort = convertToSort(command.getSortingCriterion());

        Pageable pagingSort = PageRequest.of(command.getPageNumber(), command.getPageSize(), sort);

        Page<JourneyRequestSearchOutput> journeyRequestsPage = null;

        if (isArrivalPlaceRegionAgnostic(command)) {
            journeyRequestsPage = journeyRequestRepository
                    .findByDeparturePlace_DepartmentIdAndEngineType_IdsInAndDateBetweenAndStatus_CodesAndUsernameNoProposal(
                            command.getDeparturePlaceDepartmentId(), command.getEngineTypes(),
                            command.getStartDateTime(), command.getEndDateTime(), command.getStatusCodes(),
                            command.getUserSubject(), command.getLocale(), pagingSort);
        } else {
            journeyRequestsPage = journeyRequestRepository
                    .findByDeparturePlace_DepartmentIdAndArrivalPlace_DepartmentIdsInAndEngineType_IdsInAndDateBetweenAndStatus_CodesAndUsernameNoProposal(
                            command.getDeparturePlaceDepartmentId(), command.getArrivalPlaceDepartmentIds(),
                            command.getEngineTypes(), command.getStartDateTime(), command.getEndDateTime(),
                            command.getStatusCodes(), command.getUserSubject(), command.getLocale(), pagingSort);
        }

        return new JourneyRequestsSearchOutputResult(journeyRequestsPage.getTotalPages(),
                journeyRequestsPage.getTotalElements(), journeyRequestsPage.getNumber(), journeyRequestsPage.getSize(),
                journeyRequestsPage.hasNext(), journeyRequestsPage.getContent());

    }

    protected Sort convertToSort(SortCriterion sortingCriterion) {

        return Sort.by(Direction.valueOf(sortingCriterion.getDirection().toUpperCase()),
                ParameterUtils.kebabToCamelCase(sortingCriterion.getField()));
    }

    @Override
    public JourneyRequestInputOutput createJourneyRequest(JourneyRequestInputOutput journeyRequest, String subject,
                                                          String locale) {

        // find departure department
        Optional<DepartmentJpaEntity> departureDepartmentJpaEntity = departmentResolver.resolveDepartment(
                journeyRequest.getDeparturePlace().getId(), journeyRequest.getDeparturePlace().getType());

        if (departureDepartmentJpaEntity.isEmpty()) {
            log.error(String.format("Could not find department by place ID: %d and place type: %s",
                    journeyRequest.getDeparturePlace().getId(), journeyRequest.getDeparturePlace().getType().name()));
            throw new IllegalArgumentException(
                    String.format("Department not found for departure place ID %d and placeType %s",
                            journeyRequest.getDeparturePlace().getId(), journeyRequest.getDeparturePlace().getType()));
        }
        // find arrival department
        Optional<DepartmentJpaEntity> arrivalDepartmentJpaEntity = departmentResolver.resolveDepartment(
                journeyRequest.getArrivalPlace().getId(), journeyRequest.getArrivalPlace().getType());

        if (arrivalDepartmentJpaEntity.isEmpty()) {
            log.error(String.format("Could not find department by place ID: %d and place type: %s",
                    journeyRequest.getArrivalPlace().getId(), journeyRequest.getArrivalPlace().getType().name()));
            throw new IllegalArgumentException(
                    String.format("Department not found for arrival place ID %d and placeType %s",
                            journeyRequest.getArrivalPlace().getId(), journeyRequest.getArrivalPlace().getType()));
        }
        // find engine type
        Optional<EngineTypeJpaEntity> engineTypeJpaEntity = engineTypeRepository
                .findById(journeyRequest.getEngineType().getId());

        if (engineTypeJpaEntity.isEmpty()) {
            log.error(String.format("Could not find engine type by ID: %d", journeyRequest.getEngineType().getId()));
            throw new IllegalArgumentException(
                    String.format("Engine type not found by ID %d", journeyRequest.getEngineType().getId()));
        }
        // find client
        Optional<ClientJpaEntity> clientJpaEntityOptional = clientRepository.findClientBySubject(subject);

        if (clientJpaEntityOptional.isEmpty()) {
            log.error(String.format("Could not find client by email or mobile number: %s", subject));
            throw new IllegalArgumentException(String.format("User account not found by username %s", subject));
        }

        ClientJpaEntity clientJpaEntity = clientJpaEntityOptional.get();

        // find departure place
        Optional<PlaceJpaEntity> departurePlaceOptional = placeRepository.findById(
                new PlaceId(journeyRequest.getDeparturePlace().getId(), journeyRequest.getDeparturePlace().getType()));

        PlaceJpaEntity departurePlaceJpaEntity;

        if (departurePlaceOptional.isEmpty()) {

            departurePlaceJpaEntity = placeMapper.mapToJpaEntity(journeyRequest.getDeparturePlace(),
                    departureDepartmentJpaEntity.get());

            List<LocalizedPlaceJpaEntity> departurePlaceLocalizations = localizedPlaceResolver.resolveLocalizedPlaces(
                    journeyRequest.getDeparturePlace().getId(), journeyRequest.getDeparturePlace().getType());
            for (LocalizedPlaceJpaEntity lpje : departurePlaceLocalizations) {
                lpje.setPlace(departurePlaceJpaEntity);
                departurePlaceJpaEntity.getLocalizations().put(lpje.getLocalizedPlaceId().getLocale(), lpje);
            }
            departurePlaceJpaEntity = placeRepository.save(departurePlaceJpaEntity);

        } else {
            departurePlaceJpaEntity = departurePlaceOptional.get();
        }

        // find arrival place
        Optional<PlaceJpaEntity> arrivalPlaceOptional = placeRepository.findById(
                new PlaceId(journeyRequest.getArrivalPlace().getId(), journeyRequest.getArrivalPlace().getType()));

        PlaceJpaEntity arrivalPlaceJpaEntity;

        if (arrivalPlaceOptional.isEmpty()) {

            arrivalPlaceJpaEntity = placeMapper.mapToJpaEntity(journeyRequest.getArrivalPlace(),
                    arrivalDepartmentJpaEntity.get());

            List<LocalizedPlaceJpaEntity> arrivalPlaceLocalizations = localizedPlaceResolver.resolveLocalizedPlaces(
                    journeyRequest.getArrivalPlace().getId(), journeyRequest.getArrivalPlace().getType());
            for (LocalizedPlaceJpaEntity lpje : arrivalPlaceLocalizations) {
                lpje.setPlace(arrivalPlaceJpaEntity);
                arrivalPlaceJpaEntity.getLocalizations().put(lpje.getLocalizedPlaceId().getLocale(), lpje);
            }
            arrivalPlaceJpaEntity = placeRepository.save(arrivalPlaceJpaEntity);

        } else {
            arrivalPlaceJpaEntity = arrivalPlaceOptional.get();
        }

        journeyRequest.setCreationDateTime(Instant.now());

        JourneyRequestJpaEntity journeyRequestJpaEntity = journeyRequestMapper.mapToJpaEntity(journeyRequest,
                departurePlaceJpaEntity, arrivalPlaceJpaEntity, engineTypeJpaEntity.get(), clientJpaEntity);

        JourneyRequestStatusJpaEntity journeyRequestStatusJpaEntity = journeyRequestStatusRepository
                .findByCode(JourneyRequestStatusCode.OPENED);

        journeyRequestJpaEntity.setStatus(journeyRequestStatusJpaEntity);

        // save journey request to the database
        journeyRequestJpaEntity = journeyRequestRepository.save(journeyRequestJpaEntity);

        // return JourneyRequestInputOutput to caller
        journeyRequest.setId(journeyRequestJpaEntity.getId());
        journeyRequest.setStatus(journeyRequestStatusJpaEntity.getValue(locale));
        journeyRequest.getEngineType().setName(engineTypeJpaEntity.get().getName(locale));

        return journeyRequest;
    }

    protected boolean isArrivalPlaceRegionAgnostic(SearchJourneyRequestsInput command) {
        return command.getArrivalPlaceDepartmentIds().stream().anyMatch(p -> p.equals(-1L));
    }

    @Override
    public Optional<JourneyRequestInputOutput> loadJourneyRequestById(Long id) {
        Optional<JourneyRequestJpaEntity> journeyRequestJpaEntityOptional = journeyRequestRepository.findById(id);
        if (journeyRequestJpaEntityOptional.isEmpty())
            return Optional.empty();

        JourneyRequestInputOutput journeyRequestInputOutput = journeyRequestMapper
                .mapToDomainEntity(journeyRequestJpaEntityOptional.get(), LocaleUtils.defaultLocale.toString());
        return Optional.ofNullable(journeyRequestInputOutput);
    }

    @Override
    public ClientJourneyRequestsOutput loadClientJourneyRequests(LoadClientJourneyRequestsCriteria criteria) {
        Sort sort = convertToSort(criteria.getSortingCriterion());

        Pageable pagingSort = PageRequest.of(criteria.getPageNumber(), criteria.getPageSize(), sort);
        Page<ClientJourneyRequestDtoOutput> journeyRequestsPage = null;

        journeyRequestsPage = journeyRequestRepository.findByCreationDateTimeBetweenAndClient_Email(
                criteria.getPeriodCriterion().getLowerEdge().toInstant(),
                criteria.getPeriodCriterion().getHigherEdge().toInstant(), criteria.getClientUsername(),
                criteria.getLocale(), pagingSort);

        if (journeyRequestsPage != null) {

            return new ClientJourneyRequestsOutput(journeyRequestsPage.getTotalPages(),
                    journeyRequestsPage.getTotalElements(), journeyRequestsPage.getNumber(),
                    journeyRequestsPage.getSize(), journeyRequestsPage.hasNext(), journeyRequestsPage.getContent());
        }

        return new ClientJourneyRequestsOutput(0, 0, criteria.getPageNumber(), criteria.getPageSize(), false,
                Collections.<ClientJourneyRequestDtoOutput>emptyList());
    }

    @Override
    public Optional<ClientJourneyRequestDtoOutput> loadJourneyRequestByIdAndClientSubject(Long id, String clientSubject,
                                                                                          String locale) {

        return journeyRequestRepository.findByIdAndClientSubject(id, clientSubject, locale);
    }

    @Override
    public boolean isExistentAndNotExpiredJourneyRequestByIdAndClientSubject(Long journeyRequestId,
                                                                             String clientSubject) {
        return journeyRequestRepository.existsAndNotExpiredByIdAndClientSubject(journeyRequestId, clientSubject);
    }

    @Override
    public void updateJourneyRequest(JourneyRequestInputOutput journeyRequest) {

        JourneyRequestJpaEntity journeyRequestJpaEntity = journeyRequestRepository.findById(journeyRequest.getId())
                .get();

        if (journeyRequest.getDeparturePlace() != null) {

            Optional<DepartmentJpaEntity> departureDepartmentJpaEntity = departmentResolver.resolveDepartment(
                    journeyRequest.getDeparturePlace().getId(), journeyRequest.getDeparturePlace().getType());

            if (departureDepartmentJpaEntity.isEmpty()) {
                log.error(String.format("Could not find department by place ID: %d and place type: %s",
                        journeyRequest.getDeparturePlace().getId(),
                        journeyRequest.getDeparturePlace().getType().name()));
                return;
            }

            Optional<PlaceJpaEntity> departurePlaceOptional = placeRepository.findById(new PlaceId(
                    journeyRequest.getDeparturePlace().getId(), journeyRequest.getDeparturePlace().getType()));

            PlaceJpaEntity departurePlaceJpaEntity;

            if (departurePlaceOptional.isEmpty()) {

                departurePlaceJpaEntity = placeMapper.mapToJpaEntity(journeyRequest.getDeparturePlace(),
                        departureDepartmentJpaEntity.get());

                List<LocalizedPlaceJpaEntity> departurePlaceLocalizations = localizedPlaceResolver
                        .resolveLocalizedPlaces(journeyRequest.getDeparturePlace().getId(),
                                journeyRequest.getDeparturePlace().getType());
                for (LocalizedPlaceJpaEntity lpje : departurePlaceLocalizations) {
                    lpje.setPlace(departurePlaceJpaEntity);
                    departurePlaceJpaEntity.getLocalizations().put(lpje.getLocalizedPlaceId().getLocale(), lpje);
                }
                departurePlaceJpaEntity = placeRepository.save(departurePlaceJpaEntity);

            } else {
                departurePlaceJpaEntity = departurePlaceOptional.get();
            }

            journeyRequestJpaEntity.setDeparturePlace(departurePlaceJpaEntity);
        }

        if (journeyRequest.getArrivalPlace() != null) {

            Optional<DepartmentJpaEntity> arrivalDepartmentJpaEntity = departmentResolver.resolveDepartment(
                    journeyRequest.getArrivalPlace().getId(), journeyRequest.getArrivalPlace().getType());

            if (arrivalDepartmentJpaEntity.isEmpty()) {
                log.error(String.format("Could not find department by place ID: %d and place type: %s",
                        journeyRequest.getArrivalPlace().getId(), journeyRequest.getArrivalPlace().getType().name()));
                return;
            }

            Optional<PlaceJpaEntity> arrivalPlaceOptional = placeRepository.findById(
                    new PlaceId(journeyRequest.getArrivalPlace().getId(), journeyRequest.getArrivalPlace().getType()));

            PlaceJpaEntity arrivalPlaceJpaEntity;

            if (arrivalPlaceOptional.isEmpty()) {

                arrivalPlaceJpaEntity = placeMapper.mapToJpaEntity(journeyRequest.getArrivalPlace(),
                        arrivalDepartmentJpaEntity.get());

                List<LocalizedPlaceJpaEntity> arrivalPlaceLocalizations = localizedPlaceResolver.resolveLocalizedPlaces(
                        journeyRequest.getArrivalPlace().getId(), journeyRequest.getArrivalPlace().getType());
                for (LocalizedPlaceJpaEntity lpje : arrivalPlaceLocalizations) {
                    lpje.setPlace(arrivalPlaceJpaEntity);
                    arrivalPlaceJpaEntity.getLocalizations().put(lpje.getLocalizedPlaceId().getLocale(), lpje);
                }
                arrivalPlaceJpaEntity = placeRepository.save(arrivalPlaceJpaEntity);

            } else {
                arrivalPlaceJpaEntity = arrivalPlaceOptional.get();
            }

            journeyRequestJpaEntity.setArrivalPlace(arrivalPlaceJpaEntity);
        }

        if (journeyRequest.getEngineType() != null) {
            Optional<EngineTypeJpaEntity> engineTypeJpaEntity = engineTypeRepository
                    .findById(journeyRequest.getEngineType().getId());
            if (engineTypeJpaEntity.isEmpty()) {
                log.error(
                        String.format("Could not find engine type by ID: %d", journeyRequest.getEngineType().getId()));
                return;
            }

            journeyRequestJpaEntity.setEngineType(engineTypeJpaEntity.get());
        }

        if (journeyRequest.getWorkers() != null) {
            journeyRequestJpaEntity.setWorkers(journeyRequest.getWorkers());
        }
        if (journeyRequest.getDescription() != null) {
            journeyRequestJpaEntity.setDescription(journeyRequest.getDescription());
        }

        if (journeyRequest.getDateTime() != null) {
            journeyRequestJpaEntity.setDateTime(journeyRequest.getDateTime());
        }
        if (journeyRequest.getHours() != null) {
            journeyRequestJpaEntity.setHours(journeyRequest.getHours());
        }

        if (journeyRequest.getMinutes() != null) {
            journeyRequestJpaEntity.setMinutes(journeyRequest.getMinutes());
        }

        if (journeyRequest.getDistance() != null) {
            journeyRequestJpaEntity.setDistance(journeyRequest.getDistance());
        }

        journeyRequestRepository.save(journeyRequestJpaEntity);

    }

    @Override
    public void cancelJourneyRequest(Long journeyRequestId) {

        JourneyRequestStatusJpaEntity cancelledStatusJpaEntity = journeyRequestStatusRepository
                .findByCode(JourneyRequestStatusCode.CANCELED);

        journeyRequestRepository.updateStatus(journeyRequestId, cancelledStatusJpaEntity);

    }

    @Override
    public Set<Long> loadJourneyRequestIdsByStatusCodeAndLimit(JourneyRequestStatusCode statusCode, Integer limit) {
        Page<JourneyRequestJpaEntity> jrJpaEntities = journeyRequestRepository.findByStatus_Code(statusCode,
                PageRequest.of(0, limit));
        return jrJpaEntities.getContent().stream().map(jr -> jr.getId()).collect(Collectors.toSet());
    }

    @Override
    public void updateJourneyStatus(Set<Long> fulfilledJourneysIds, JourneyRequestStatusCode status) {
        JourneyRequestStatusJpaEntity StatusJpaEntity = journeyRequestStatusRepository.findByCode(status);
        journeyRequestRepository.updateInBatch(fulfilledJourneysIds, StatusJpaEntity);
    }

}
