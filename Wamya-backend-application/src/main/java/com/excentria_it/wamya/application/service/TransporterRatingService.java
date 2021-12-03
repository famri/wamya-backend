package com.excentria_it.wamya.application.service;

import java.time.ZoneId;
import java.util.Optional;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.LoadTransporterRatingDetailsUseCase;
import com.excentria_it.wamya.application.port.out.LoadTransporterRatingRequestRecordPort;
import com.excentria_it.wamya.application.utils.DateTimeHelper;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.exception.TransporterRatingDetailsNotFoundException;
import com.excentria_it.wamya.domain.TransporterRatingRequestRecordDto;
import com.excentria_it.wamya.domain.TransporterRatingRequestRecordDto.JourneyRequestDto;
import com.excentria_it.wamya.domain.TransporterRatingRequestRecordDto.PlaceDto;
import com.excentria_it.wamya.domain.TransporterRatingRequestRecordDto.TransporterDto;
import com.excentria_it.wamya.domain.TransporterRatingRequestRecordOutput;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class TransporterRatingService implements LoadTransporterRatingDetailsUseCase {

	private final LoadTransporterRatingRequestRecordPort loadTransporterRatingDetailsPort;

	private final DateTimeHelper dateTimeHelper;

	@Override
	public TransporterRatingRequestRecordDto loadTransporterRatingDetails(LoadTransporterRatingRequestCommand command,
			String locale) {

		Optional<TransporterRatingRequestRecordOutput> trdOptional = loadTransporterRatingDetailsPort
				.loadRecord(command.getHash(), command.getUserId(), locale);

		if (trdOptional.isEmpty()) {
			throw new TransporterRatingDetailsNotFoundException("Transporter rating details not found.");
		}
		
		TransporterRatingRequestRecordOutput trdo = trdOptional.get();
		
		ZoneId userZoneId = dateTimeHelper.findUserZoneId(command.getUserId());

		PlaceDto departurePlaceDto = new PlaceDto(trdo.getJourneyRequest().getDeparturePlace().getName());
		PlaceDto arrivalPlaceDto = new PlaceDto(trdo.getJourneyRequest().getArrivalPlace().getName());

		JourneyRequestDto jrDto = new JourneyRequestDto(departurePlaceDto, arrivalPlaceDto,
				dateTimeHelper.systemToUserLocalDateTime(trdo.getJourneyRequest().getDateTime(), userZoneId));

		TransporterDto transporterDto = new TransporterDto(trdo.getTransporter().getFirstname(),
				trdo.getTransporter().getPhotoUrl(), trdo.getTransporter().getGlobalRating());

		return new TransporterRatingRequestRecordDto(jrDto, transporterDto);
	}

}
