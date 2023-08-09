package com.excentria_it.wamya.adapter.persistence.adapter;

import java.util.Optional;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.RatingJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.repository.ClientRepository;
import com.excentria_it.wamya.adapter.persistence.repository.TransporterRepository;
import com.excentria_it.wamya.application.port.out.SaveTransporterRatingPort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class TransporterRatingPersistenceAdapter implements SaveTransporterRatingPort {

	private final TransporterRepository transporterRepository;
	private final ClientRepository clientRepository;

	@Override
	public void saveRating(Long clientId, Integer rating, String comment, Long transporterId) {

		Optional<TransporterJpaEntity> transporterOptional = transporterRepository.findById(transporterId);

		TransporterJpaEntity transporter = transporterOptional.get();

		Optional<ClientJpaEntity> clientOptional = clientRepository.findById(clientId);

		ClientJpaEntity client = clientOptional.get();

		RatingJpaEntity ratingJpaEntity = RatingJpaEntity.builder().user(client).value(rating).comment(comment).build();

		transporter.getRatings().add(ratingJpaEntity);

		transporterRepository.save(transporter);

	}

}
