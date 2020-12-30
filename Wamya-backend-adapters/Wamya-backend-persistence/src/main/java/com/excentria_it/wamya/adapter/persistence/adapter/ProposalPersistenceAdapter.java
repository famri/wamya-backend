package com.excentria_it.wamya.adapter.persistence.adapter;

import java.util.Optional;

import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.VehiculeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.JourneyProposalMapper;
import com.excentria_it.wamya.adapter.persistence.repository.JourneyProposalRepository;
import com.excentria_it.wamya.adapter.persistence.repository.JourneyRequestRepository;
import com.excentria_it.wamya.adapter.persistence.repository.TransporterRepository;
import com.excentria_it.wamya.adapter.persistence.repository.VehiculeRepository;
import com.excentria_it.wamya.application.port.out.MakeProposalPort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class ProposalPersistenceAdapter implements MakeProposalPort {

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
		journeyRequestJpaEntity.getProposals().add(journeyProposalJpaEntity);

		journeyRequestRepository.save(journeyRequestJpaEntity);

		return journeyProposalJpaEntity.getId();
	}

}
