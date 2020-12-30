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
import com.excentria_it.wamya.adapter.persistence.entity.PlaceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.JourneyRequestMapper;
import com.excentria_it.wamya.adapter.persistence.mapper.PlaceMapper;
import com.excentria_it.wamya.adapter.persistence.repository.ClientRepository;
import com.excentria_it.wamya.adapter.persistence.repository.EngineTypeRepository;
import com.excentria_it.wamya.adapter.persistence.repository.JourneyRequestRepository;
import com.excentria_it.wamya.adapter.persistence.repository.PlaceRepository;
import com.excentria_it.wamya.application.port.in.SearchJourneyRequestsUseCase.SearchJourneyRequestsCommand;
import com.excentria_it.wamya.application.port.out.CreateJourneyRequestPort;
import com.excentria_it.wamya.application.port.out.LoadJourneyRequestPort;
import com.excentria_it.wamya.application.port.out.SearchJourneyRequestsPort;
import com.excentria_it.wamya.common.SortingCriterion;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.common.utils.LocaleUtils;
import com.excentria_it.wamya.domain.CreateJourneyRequestDto;
import com.excentria_it.wamya.domain.JourneyRequestSearchDto;
import com.excentria_it.wamya.domain.JourneyRequestsSearchResult;
import com.excentria_it.wamya.domain.SearchJourneyRequestsCriteria;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class JourneyRequestsPersistenceAdapter
		implements SearchJourneyRequestsPort, CreateJourneyRequestPort, LoadJourneyRequestPort {

	private final JourneyRequestRepository journeyRequestRepository;

	private final EngineTypeRepository engineTypeRepository;

	private final ClientRepository clientRepository;

	private final PlaceRepository placeRepository;

	private final JourneyRequestMapper journeyRequestMapper;

	private final PlaceMapper placeMapper;

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

		return new JourneyRequestsSearchResult(0, 0, 0, 0, false, Collections.<JourneyRequestSearchDto>emptyList());
	}

	protected Sort convertToSort(SortingCriterion sortingCriterion) {
		if ("min-price".equals(sortingCriterion.getField())) {
			return (JpaSort.unsafe(Direction.valueOf(sortingCriterion.getDirection().toUpperCase()),
					stripDashes("(" + sortingCriterion.getField() + ")")));
		}
		return Sort.by(Direction.valueOf(sortingCriterion.getDirection().toUpperCase()),
				stripDashes(sortingCriterion.getField()));
	}

	protected String stripDashes(String str) {
		int idx;
		while ((idx = str.indexOf("-")) != -1) {
			if (idx < str.length() - 1) {
				String charToUpper = str.substring(idx + 1, idx + 2).toUpperCase();
				str = str.substring(0, idx) + charToUpper + str.substring(idx + 2);
			} else {
				str = str.substring(0, idx);
			}
		}
		return str;
	}

	@Override
	public CreateJourneyRequestDto createJourneyRequest(CreateJourneyRequestDto journeyRequest, String username) {

		EngineTypeJpaEntity engineTypeJpaEntity = engineTypeRepository.findById(journeyRequest.getEngineType().getId())
				.get();

		Optional<ClientJpaEntity> clientJpaEntityOptional = clientRepository.findByEmail(username);

		if (!clientJpaEntityOptional.isPresent()) {

			String[] userMobilePhone = username.split("_");

			clientJpaEntityOptional = clientRepository.findByMobilePhoneNumber(userMobilePhone[0], userMobilePhone[1]);

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

		journeyRequestJpaEntity = journeyRequestRepository.save(journeyRequestJpaEntity);

		journeyRequest.setId(journeyRequestJpaEntity.getId());

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
}
