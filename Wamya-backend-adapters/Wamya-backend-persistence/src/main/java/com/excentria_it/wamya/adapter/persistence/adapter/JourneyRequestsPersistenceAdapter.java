package com.excentria_it.wamya.adapter.persistence.adapter;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.JpaSort;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestStatusJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestStatusJpaEntity.JourneyRequestStatusCode;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedPlaceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.PlaceId;
import com.excentria_it.wamya.adapter.persistence.entity.PlaceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.JourneyRequestMapper;
import com.excentria_it.wamya.adapter.persistence.mapper.PlaceMapper;
import com.excentria_it.wamya.adapter.persistence.repository.ClientRepository;
import com.excentria_it.wamya.adapter.persistence.repository.EngineTypeRepository;
import com.excentria_it.wamya.adapter.persistence.repository.JourneyRequestRepository;
import com.excentria_it.wamya.adapter.persistence.repository.JourneyRequestStatusRepository;
import com.excentria_it.wamya.adapter.persistence.repository.PlaceRepository;
import com.excentria_it.wamya.adapter.persistence.utils.DepartmentJpaEntityResolver;
import com.excentria_it.wamya.adapter.persistence.utils.LocalizedPlaceJpaEntityResolver;
import com.excentria_it.wamya.application.port.out.CreateJourneyRequestPort;
import com.excentria_it.wamya.application.port.out.LoadClientJourneyRequestsPort;
import com.excentria_it.wamya.application.port.out.LoadJourneyRequestPort;
import com.excentria_it.wamya.application.port.out.SearchJourneyRequestsPort;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.common.utils.LocaleUtils;
import com.excentria_it.wamya.common.utils.ParameterUtils;
import com.excentria_it.wamya.domain.ClientJourneyRequestDto;
import com.excentria_it.wamya.domain.ClientJourneyRequests;
import com.excentria_it.wamya.domain.JourneyRequestInputOutput;
import com.excentria_it.wamya.domain.JourneyRequestSearchOutput;
import com.excentria_it.wamya.domain.JourneyRequestsSearchOutputResult;
import com.excentria_it.wamya.domain.LoadClientJourneyRequestsCriteria;
import com.excentria_it.wamya.domain.SearchJourneyRequestsInput;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@PersistenceAdapter
@Slf4j
public class JourneyRequestsPersistenceAdapter implements SearchJourneyRequestsPort, CreateJourneyRequestPort,
		LoadJourneyRequestPort, LoadClientJourneyRequestsPort {

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
					.findByDeparturePlace_DepartmentIdAndEngineType_IdInAndDateBetween(
							command.getDeparturePlaceDepartmentId(), command.getEngineTypes(),
							command.getStartDateTime(), command.getEndDateTime(), command.getLocale(), pagingSort);
		} else {
			journeyRequestsPage = journeyRequestRepository
					.findByDeparturePlace_DepartmentIdAndArrivalPlace_DepartmentIdInAndEngineType_IdInAndDateBetween(
							command.getDeparturePlaceDepartmentId(), command.getArrivalPlaceDepartmentIds(),
							command.getEngineTypes(), command.getStartDateTime(), command.getEndDateTime(),
							command.getLocale(), pagingSort);
		}

		return new JourneyRequestsSearchOutputResult(journeyRequestsPage.getTotalPages(),
				journeyRequestsPage.getTotalElements(), journeyRequestsPage.getNumber(), journeyRequestsPage.getSize(),
				journeyRequestsPage.hasNext(), journeyRequestsPage.getContent());

	}

	protected Sort convertToSort(SortCriterion sortingCriterion) {
		if ("min-price".equals(sortingCriterion.getField())) {
			return (JpaSort.unsafe(Direction.valueOf(sortingCriterion.getDirection().toUpperCase()),
					ParameterUtils.kebabToCamelCase("(" + sortingCriterion.getField() + ")")));
		}
		return Sort.by(Direction.valueOf(sortingCriterion.getDirection().toUpperCase()),
				ParameterUtils.kebabToCamelCase(sortingCriterion.getField()));
	}

	@Override
	public JourneyRequestInputOutput createJourneyRequest(JourneyRequestInputOutput journeyRequest, String username,
			String locale) {

		Optional<DepartmentJpaEntity> departureDepartmentJpaEntity = departmentResolver.resolveDepartment(
				journeyRequest.getDeparturePlace().getId(), journeyRequest.getDeparturePlace().getType());

		if (departureDepartmentJpaEntity.isEmpty()) {
			log.error(String.format("Could not find department by place ID: %d and place type: %s",
					journeyRequest.getDeparturePlace().getId(), journeyRequest.getDeparturePlace().getType().name()));
			return null;
		}

		Optional<DepartmentJpaEntity> arrivalDepartmentJpaEntity = departmentResolver.resolveDepartment(
				journeyRequest.getArrivalPlace().getId(), journeyRequest.getArrivalPlace().getType());

		if (arrivalDepartmentJpaEntity.isEmpty()) {
			log.error(String.format("Could not find department by place ID: %d and place type: %s",
					journeyRequest.getArrivalPlace().getId(), journeyRequest.getArrivalPlace().getType().name()));
			return null;
		}

		Optional<EngineTypeJpaEntity> engineTypeJpaEntity = engineTypeRepository
				.findById(journeyRequest.getEngineType().getId());

		if (engineTypeJpaEntity.isEmpty()) {
			log.error(String.format("Could not find engine type by ID: %d", journeyRequest.getEngineType().getId()));
			return null;
		}

		Optional<ClientJpaEntity> clientJpaEntityOptional = clientRepository.findByEmail(username);

		if (clientJpaEntityOptional.isEmpty()) {
			if (username.contains("_")) {
				String[] userMobilePhone = username.split("_");

				clientJpaEntityOptional = clientRepository.findByIcc_ValueAndMobileNumber(userMobilePhone[0],
						userMobilePhone[1]);
			} else {
				log.error(String.format("Could not find client by email or mobile number: %s", username));
				return null;
			}

		}

		if (clientJpaEntityOptional.isEmpty()) {
			log.error(String.format("Could not find client by email or mobile number: %s", username));
			return null;
		}

		ClientJpaEntity clientJpaEntity = clientJpaEntityOptional.get();

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
		journeyRequestJpaEntity = journeyRequestRepository.save(journeyRequestJpaEntity);

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
	public ClientJourneyRequests loadClientJourneyRequests(LoadClientJourneyRequestsCriteria criteria) {
		Sort sort = convertToSort(criteria.getSortingCriterion());

		Pageable pagingSort = PageRequest.of(criteria.getPageNumber(), criteria.getPageSize(), sort);
		Page<ClientJourneyRequestDto> journeyRequestsPage = null;
		if (criteria.getClientUsername().contains("@")) {
			journeyRequestsPage = journeyRequestRepository.findByCreationDateTimeBetweenAndClient_Email(
					criteria.getPeriodCriterion().getLowerEdge().toInstant(),
					criteria.getPeriodCriterion().getHigherEdge().toInstant(), criteria.getClientUsername(),
					criteria.getLocale(), pagingSort);
		} else {

			String[] mobileNumber = criteria.getClientUsername().split("_");
			journeyRequestsPage = journeyRequestRepository
					.findByCreationDateTimeBetweenAndClient_MobileNumberAndClient_IccValue(
							criteria.getPeriodCriterion().getLowerEdge().toInstant(),
							criteria.getPeriodCriterion().getHigherEdge().toInstant(), mobileNumber[0], mobileNumber[1],
							criteria.getLocale(), pagingSort);
		}

		if (journeyRequestsPage != null) {

			return new ClientJourneyRequests(journeyRequestsPage.getTotalPages(),
					journeyRequestsPage.getTotalElements(), journeyRequestsPage.getNumber(),
					journeyRequestsPage.getSize(), journeyRequestsPage.hasNext(), journeyRequestsPage.getContent());
		}

		return new ClientJourneyRequests(0, 0, criteria.getPageNumber(), criteria.getPageSize(), false,
				Collections.<ClientJourneyRequestDto>emptyList());
	}

	@Override
	public Optional<ClientJourneyRequestDto> loadJourneyRequestByIdAndClientEmail(Long id, String email) {

		return journeyRequestRepository.findByIdAndClient_Email(id, email);
	}

	@Override
	public Optional<ClientJourneyRequestDto> loadJourneyRequestByIdAndClientMobileNumberAndIcc(Long id,
			String mobileNumber, String icc) {
		return journeyRequestRepository.findByIdAndClient_MobileNumberAndClient_IccValue(id, mobileNumber, icc);
	}
}
