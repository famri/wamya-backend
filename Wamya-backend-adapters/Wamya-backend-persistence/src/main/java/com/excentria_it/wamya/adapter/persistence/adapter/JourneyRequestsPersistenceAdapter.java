package com.excentria_it.wamya.adapter.persistence.adapter;

import java.util.Collections;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.JpaSort;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestStatusJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestStatusJpaEntity.JourneyRequestStatusCode;
import com.excentria_it.wamya.adapter.persistence.entity.PlaceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.JourneyRequestMapper;
import com.excentria_it.wamya.adapter.persistence.mapper.PlaceMapper;
import com.excentria_it.wamya.adapter.persistence.repository.ClientRepository;
import com.excentria_it.wamya.adapter.persistence.repository.EngineTypeRepository;
import com.excentria_it.wamya.adapter.persistence.repository.JourneyRequestRepository;
import com.excentria_it.wamya.adapter.persistence.repository.JourneyRequestStatusRepository;
import com.excentria_it.wamya.adapter.persistence.repository.PlaceRepository;
import com.excentria_it.wamya.application.port.in.SearchJourneyRequestsUseCase.SearchJourneyRequestsCommand;
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
import com.excentria_it.wamya.domain.CreateJourneyRequestDto;
import com.excentria_it.wamya.domain.JourneyRequestSearchDto;
import com.excentria_it.wamya.domain.JourneyRequestsSearchResult;
import com.excentria_it.wamya.domain.LoadClientJourneyRequestsCriteria;
import com.excentria_it.wamya.domain.SearchJourneyRequestsCriteria;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class JourneyRequestsPersistenceAdapter implements SearchJourneyRequestsPort, CreateJourneyRequestPort,
		LoadJourneyRequestPort, LoadClientJourneyRequestsPort {

	private final JourneyRequestRepository journeyRequestRepository;

	private final EngineTypeRepository engineTypeRepository;

	private final ClientRepository clientRepository;

	private final PlaceRepository placeRepository;

	private final JourneyRequestMapper journeyRequestMapper;

	private final PlaceMapper placeMapper;

	private final JourneyRequestStatusRepository journeyRequestStatusRepository;

	@Override
	public JourneyRequestsSearchResult searchJourneyRequests(SearchJourneyRequestsCriteria command) {

		Sort sort = convertToSort(command.getSortingCriterion());

		Pageable pagingSort = PageRequest.of(command.getPageNumber(), command.getPageSize(), sort);

		Page<JourneyRequestSearchDto> journeyRequestsPage = null;

		if (isArrivalPlaceRegionAgnostic(command)) {
			journeyRequestsPage = journeyRequestRepository
					.findByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween(command.getDeparturePlaceRegionId(),
							command.getEngineTypes(), command.getStartDateTime(), command.getEndDateTime(),
							command.getLocale(), pagingSort);
		} else {
			journeyRequestsPage = journeyRequestRepository
					.findByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween(
							command.getDeparturePlaceRegionId(), command.getArrivalPlaceRegionIds(),
							command.getEngineTypes(), command.getStartDateTime(), command.getEndDateTime(),
							command.getLocale(), pagingSort);
		}

		if (journeyRequestsPage != null) {

			return new JourneyRequestsSearchResult(journeyRequestsPage.getTotalPages(),
					journeyRequestsPage.getTotalElements(), journeyRequestsPage.getNumber(),
					journeyRequestsPage.getSize(), journeyRequestsPage.hasNext(), journeyRequestsPage.getContent());
		}

		return new JourneyRequestsSearchResult(0, 0, command.getPageNumber(), command.getPageSize(), false,
				Collections.<JourneyRequestSearchDto>emptyList());
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
	public CreateJourneyRequestDto createJourneyRequest(CreateJourneyRequestDto journeyRequest, String username,
			String locale) {

		EngineTypeJpaEntity engineTypeJpaEntity = engineTypeRepository.findById(journeyRequest.getEngineType().getId())
				.get();

		Optional<ClientJpaEntity> clientJpaEntityOptional = clientRepository.findByEmail(username);

		if (!clientJpaEntityOptional.isPresent()) {

			String[] userMobilePhone = username.split("_");

			clientJpaEntityOptional = clientRepository.findByIcc_ValueAndMobileNumber(userMobilePhone[0],
					userMobilePhone[1]);

		}

		ClientJpaEntity clientJpaEntity = clientJpaEntityOptional.get();

		Optional<PlaceJpaEntity> departurePlaceOptional = placeRepository
				.findById(journeyRequest.getDeparturePlace().getPlaceId());

		PlaceJpaEntity departurePlaceJpaEntity;

		if (departurePlaceOptional.isEmpty()) {
			departurePlaceJpaEntity = placeRepository
					.save(placeMapper.mapToJpaEntity(journeyRequest.getDeparturePlace()));
		} else {
			departurePlaceJpaEntity = departurePlaceOptional.get();
		}

		Optional<PlaceJpaEntity> arrivalPlaceOptional = placeRepository
				.findById(journeyRequest.getArrivalPlace().getPlaceId());

		PlaceJpaEntity arrivalPlaceJpaEntity;

		if (arrivalPlaceOptional.isEmpty()) {
			arrivalPlaceJpaEntity = placeRepository.save(placeMapper.mapToJpaEntity(journeyRequest.getArrivalPlace()));
		} else {
			arrivalPlaceJpaEntity = arrivalPlaceOptional.get();
		}

		JourneyRequestJpaEntity journeyRequestJpaEntity = journeyRequestMapper.mapToJpaEntity(journeyRequest,
				departurePlaceJpaEntity, arrivalPlaceJpaEntity, engineTypeJpaEntity, clientJpaEntity);

		JourneyRequestStatusJpaEntity journeyRequestStatusJpaEntity = journeyRequestStatusRepository
				.findByCode(JourneyRequestStatusCode.OPENED);

		journeyRequestJpaEntity.setStatus(journeyRequestStatusJpaEntity);
		journeyRequestJpaEntity = journeyRequestRepository.save(journeyRequestJpaEntity);

		journeyRequest.setId(journeyRequestJpaEntity.getId());
		journeyRequest.setStatus(journeyRequestStatusJpaEntity.getValue(locale));

		return journeyRequest;
	}

	protected boolean isArrivalPlaceRegionAgnostic(SearchJourneyRequestsCriteria command) {
		return command.getArrivalPlaceRegionIds().stream()
				.anyMatch(p -> SearchJourneyRequestsCommand.ANY_ARRIVAL_REGION.equals(p.toUpperCase()));
	}

	@Override
	public Optional<CreateJourneyRequestDto> loadJourneyRequestById(Long id) {
		Optional<JourneyRequestJpaEntity> journeyRequestJpaEntityOptional = journeyRequestRepository.findById(id);
		if (journeyRequestJpaEntityOptional.isEmpty())
			return Optional.empty();

		CreateJourneyRequestDto createJourneyRequestDto = journeyRequestMapper
				.mapToDomainEntity(journeyRequestJpaEntityOptional.get(), LocaleUtils.defaultLocale.toString());
		return Optional.ofNullable(createJourneyRequestDto);
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
