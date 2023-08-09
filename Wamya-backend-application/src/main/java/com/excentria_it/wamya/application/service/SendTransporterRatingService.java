package com.excentria_it.wamya.application.service;

import java.util.Optional;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.SendTransporterRatingUseCase;
import com.excentria_it.wamya.application.port.out.LoadTransporterRatingRequestRecordPort;
import com.excentria_it.wamya.application.port.out.SaveTransporterRatingPort;
import com.excentria_it.wamya.application.port.out.UpdateTransporterRatingRequestRecordPort;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.exception.TransporterRatingRequestNotFoundException;
import com.excentria_it.wamya.domain.TransporterRatingRequestRecordOutput;
import com.excentria_it.wamya.domain.TransporterRatingRequestStatus;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class SendTransporterRatingService implements SendTransporterRatingUseCase {

	private final LoadTransporterRatingRequestRecordPort loadTransporterRatingRequestPort;

	private final SaveTransporterRatingPort saveTransporterRatingPort;

	private final UpdateTransporterRatingRequestRecordPort updateTransporterRatingRequestPort;

	@Override
	public void saveTransporterRating(SendTransporterRatingCommand command, String locale) {

		Optional<TransporterRatingRequestRecordOutput> trdOptional = loadTransporterRatingRequestPort
				.loadRecord(command.getHash(), command.getUid(), locale);

		if (trdOptional.isEmpty()) {
			throw new TransporterRatingRequestNotFoundException("Transporter rating details not found.");
		}

		TransporterRatingRequestRecordOutput trr = trdOptional.get();

		saveTransporterRatingPort.saveRating(trr.getClient().getId(), command.getRating(), command.getComment(),
				trr.getTransporter().getId());

		updateTransporterRatingRequestPort.updateRequestStatus(trr.getId(), TransporterRatingRequestStatus.FULFILLED);

	}

}
