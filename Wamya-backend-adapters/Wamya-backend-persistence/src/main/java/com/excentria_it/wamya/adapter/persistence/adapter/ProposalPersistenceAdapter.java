package com.excentria_it.wamya.adapter.persistence.adapter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.VehiculeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.JourneyProposalMapper;
import com.excentria_it.wamya.adapter.persistence.repository.JourneyProposalRepository;
import com.excentria_it.wamya.adapter.persistence.repository.JourneyRequestRepository;
import com.excentria_it.wamya.adapter.persistence.repository.TransporterRepository;
import com.excentria_it.wamya.adapter.persistence.repository.VehiculeRepository;
import com.excentria_it.wamya.application.port.out.LoadProposalsPort;
import com.excentria_it.wamya.application.port.out.MakeProposalPort;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.common.utils.ParameterUtils;
import com.excentria_it.wamya.domain.JourneyProposalDto;
import com.excentria_it.wamya.domain.JourneyRequestProposals;
import com.excentria_it.wamya.domain.LoadJourneyProposalsCriteria;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class ProposalPersistenceAdapter implements MakeProposalPort, LoadProposalsPort {

	private final JourneyProposalRepository journeyProposalRepository;

	private final JourneyRequestRepository journeyRequestRepository;

	private final TransporterRepository transporterRepository;

	private final VehiculeRepository vehiculeRepository;

	private final JourneyProposalMapper journeyProposalMapper;

	@Override
	public Long makeProposal(String username, Double price, Long vehiculeId, Long journeyRequestId) {

		Optional<TransporterJpaEntity> transporterJpaEntityOptional = null;

		if (username.contains("@")) {

			transporterJpaEntityOptional = transporterRepository.findByEmail(username);

		} else {

			String[] mobilePhoneNumber = username.split("_");
			transporterJpaEntityOptional = transporterRepository.findByMobilePhoneNumber(mobilePhoneNumber[0],
					mobilePhoneNumber[1]);
		}

		Optional<VehiculeJpaEntity> vehiculeJpaEntityOptional = vehiculeRepository.findById(vehiculeId);

		JourneyProposalJpaEntity journeyProposalJpaEntity = journeyProposalMapper.mapToJpaEntity(price,
				transporterJpaEntityOptional.get(), vehiculeJpaEntityOptional.get());

		journeyProposalJpaEntity = journeyProposalRepository.save(journeyProposalJpaEntity);

		Optional<JourneyRequestJpaEntity> journeyRequestJpaEntityOptional = journeyRequestRepository
				.findById(journeyRequestId);

		JourneyRequestJpaEntity journeyRequestJpaEntity = journeyRequestJpaEntityOptional.get();
		journeyRequestJpaEntity.addProposal(journeyProposalJpaEntity);

		journeyRequestRepository.save(journeyRequestJpaEntity);

		return journeyProposalJpaEntity.getId();
	}

	@Override
	public JourneyRequestProposals loadJourneyProposals(LoadJourneyProposalsCriteria criteria) {
		Sort sort = convertToSort(criteria.getSortingCriterion());
		Pageable pagingSort = PageRequest.of(criteria.getPageNumber(), criteria.getPageSize(), sort);
		Page<JourneyProposalJpaEntity> page = journeyProposalRepository
				.findByJourneyRequest_Id(criteria.getJourneyRequestId(), pagingSort);
		
		if (page == null) {
			return new JourneyRequestProposals(0, 0, criteria.getPageNumber(), criteria.getPageSize(), false,
					Collections.<JourneyProposalDto>emptyList());
		}

		List<JourneyProposalDto> journeyProposalDtoList = page.getContent().stream()
				.map(p -> journeyProposalMapper.mapToDomainEntity(p)).collect(Collectors.toList());

		return new JourneyRequestProposals(page.getTotalPages(), page.getTotalElements(), page.getNumber(),
				page.getSize(), page.hasNext(), journeyProposalDtoList);

	}

	private Sort convertToSort(SortCriterion sortingCriterion) {

		return Sort.by(Direction.valueOf(sortingCriterion.getDirection().toUpperCase()),
				ParameterUtils.kebabToCamelCase(sortingCriterion.getField()));
	}

}
