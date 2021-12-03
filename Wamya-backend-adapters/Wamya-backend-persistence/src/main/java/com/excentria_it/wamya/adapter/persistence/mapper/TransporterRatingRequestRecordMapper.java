package com.excentria_it.wamya.adapter.persistence.mapper;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterRatingRequestRecordJpaEntity;
import com.excentria_it.wamya.application.utils.DocumentUrlResolver;
import com.excentria_it.wamya.domain.TransporterRatingRequestRecordOutput;
import com.excentria_it.wamya.domain.TransporterRatingRequestRecordOutput.ClientDto;
import com.excentria_it.wamya.domain.TransporterRatingRequestRecordOutput.JourneyRequestOutput;
import com.excentria_it.wamya.domain.TransporterRatingRequestRecordOutput.PlaceDto;
import com.excentria_it.wamya.domain.TransporterRatingRequestRecordOutput.TransporterDto;
import com.excentria_it.wamya.domain.UserPreferenceKey;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class TransporterRatingRequestRecordMapper {

	private final DocumentUrlResolver documentUrlResolver;

	public TransporterRatingRequestRecordOutput mapToDomainEntity(TransporterRatingRequestRecordJpaEntity trdJpaEntity,
			String locale) {

		if (trdJpaEntity == null || locale == null || locale.isEmpty()) {
			return null;
		}

		JourneyRequestJpaEntity jrJpaEntity = trdJpaEntity.getJourenyRequest();

		JourneyRequestOutput jrio = JourneyRequestOutput.builder()
				.departurePlace(new PlaceDto(jrJpaEntity.getDeparturePlace().getName(locale)))
				.arrivalPlace(new PlaceDto(jrJpaEntity.getArrivalPlace().getName(locale)))
				.dateTime(jrJpaEntity.getDateTime()).build();

		TransporterDto transporterDto = new TransporterDto(trdJpaEntity.getTransporter().getId(),
				trdJpaEntity.getTransporter().getFirstname(),
				documentUrlResolver.resolveUrl(trdJpaEntity.getTransporter().getProfileImage().getId(),
						trdJpaEntity.getTransporter().getProfileImage().getHash()),
				trdJpaEntity.getTransporter().getGlobalRating());

		ClientDto clientDto = new ClientDto(jrJpaEntity.getClient().getId(), jrJpaEntity.getClient().getFirstname(),
				jrJpaEntity.getClient().getEmail(),
				jrJpaEntity.getClient().getPreferenceValue(UserPreferenceKey.LOCALE),
				jrJpaEntity.getClient().getPreferenceValue(UserPreferenceKey.TIMEZONE));

		return TransporterRatingRequestRecordOutput.builder().id(trdJpaEntity.getId()).journeyRequest(jrio)
				.transporter(transporterDto).client(clientDto).hash(trdJpaEntity.getHash()).build();

	}
}
