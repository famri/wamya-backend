package com.excentria_it.wamya.adapter.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalStatusJpaEntity.JourneyProposalStatusCode;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterRatingRequestRecordJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterRatingRequestRecordJpaEntity.TransporterRatingRequestStatus;
import com.excentria_it.wamya.adapter.persistence.mapper.TransporterRatingRequestRecordMapper;
import com.excentria_it.wamya.adapter.persistence.repository.JourneyRequestRepository;
import com.excentria_it.wamya.adapter.persistence.repository.TransporterRatingRequestRecordRepository;
import com.excentria_it.wamya.application.port.out.CreateTransporterRatingRequestPort;
import com.excentria_it.wamya.application.port.out.LoadTransporterRatingRequestRecordPort;
import com.excentria_it.wamya.application.port.out.UpdateTransporterRatingRequestRecordPort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.common.utils.ConsumerUtils;
import com.excentria_it.wamya.domain.TransporterRatingRequestRecordOutput;
import com.excentria_it.wamya.domain.UserPreferenceKey;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class TransporterRatingRequestPersistenceAdapter implements LoadTransporterRatingRequestRecordPort,
		CreateTransporterRatingRequestPort, UpdateTransporterRatingRequestRecordPort {

	private final TransporterRatingRequestRecordRepository transporterRatingDetailsRepository;
	private final TransporterRatingRequestRecordMapper transporterRatingDetailsMapper;
	private final JourneyRequestRepository journeyRequestRepository;

	@Override
	public Optional<TransporterRatingRequestRecordOutput> loadRecord(String hash, Long userId, String locale) {

		Optional<TransporterRatingRequestRecordJpaEntity> trdJpaEntityOptional = transporterRatingDetailsRepository
				.findByHashAndClient_Id(hash, userId);

		if (trdJpaEntityOptional.isEmpty()) {

			return Optional.empty();

		}

		return Optional.of(transporterRatingDetailsMapper.mapToDomainEntity(trdJpaEntityOptional.get(), locale));

	}

	@Override
	public void createTransporterRatingRequests(Set<Long> fulfilledJourneysIds, List<String> hashList) {

		Set<JourneyRequestJpaEntity> journeyRequests = journeyRequestRepository.findByIds(fulfilledJourneysIds);

		journeyRequests.stream().forEach(ConsumerUtils.withCounter((index, jr) -> {
			ClientJpaEntity client = jr.getClient();
			TransporterJpaEntity transporter = jr.getProposals().stream()
					.filter(p -> p.getStatus().getCode().equals(JourneyProposalStatusCode.ACCEPTED)).findFirst().get()
					.getTransporter();

			TransporterRatingRequestRecordJpaEntity trdje = new TransporterRatingRequestRecordJpaEntity(jr, transporter,
					client, hashList.get(index), 0, TransporterRatingRequestStatus.SAVED);

			transporterRatingDetailsRepository.save(trdje);

		}));
	}

	@Override
	public Set<TransporterRatingRequestRecordOutput> loadUnfulfilledRecords(Integer maxRevives) {

		Set<TransporterRatingRequestRecordJpaEntity> records = transporterRatingDetailsRepository
				.findByStatusAndRevivesLessThan(TransporterRatingRequestStatus.SAVED, maxRevives);

		return records.stream().map(r -> transporterRatingDetailsMapper.mapToDomainEntity(r,
				r.getClient().getPreferenceValue(UserPreferenceKey.LOCALE))).collect(Collectors.toSet());

	}

	@Override
	public void incrementRevivals(Set<Long> ids) {
		transporterRatingDetailsRepository.incrementRevivals(ids);
	}

}
