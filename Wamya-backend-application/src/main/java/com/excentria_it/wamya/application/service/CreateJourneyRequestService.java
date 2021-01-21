package com.excentria_it.wamya.application.service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.CreateJourneyRequestUseCase;
import com.excentria_it.wamya.application.port.out.CreateJourneyRequestPort;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.common.exception.UserMobileNumberValidationException;
import com.excentria_it.wamya.domain.CreateJourneyRequestDto;
import com.excentria_it.wamya.domain.CreateJourneyRequestDto.EngineTypeDto;
import com.excentria_it.wamya.domain.CreateJourneyRequestDto.PlaceDto;
import com.excentria_it.wamya.domain.UserAccount;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class CreateJourneyRequestService implements CreateJourneyRequestUseCase {

	private final CreateJourneyRequestPort createJourneyRequestPort;

	private final LoadUserAccountPort loadUserAccountPort;

	@Override
	public CreateJourneyRequestDto createJourneyRequest(CreateJourneyRequestCommand command, String username,
			String locale) {

		checkUserMobileNumberIsVerified(username);

		CreateJourneyRequestDto journeyRequest = CreateJourneyRequestDto.builder()
				.departurePlace(new PlaceDto(command.getDeparturePlaceId(), command.getDeparturePlaceRegionId(),
						command.getDeparturePlaceName()))

				.arrivalPlace(new PlaceDto(command.getArrivalPlaceId(), command.getArrivalPlaceRegionId(),
						command.getArrivalPlaceName()))
				.dateTime(command.getDateTime().withZoneSameInstant(ZoneOffset.UTC).toInstant())

				.creationDateTime(Instant.now()).engineType(new EngineTypeDto(command.getEngineTypeId(), null))
				.distance(command.getDistance()).workers(command.getWorkers()).description(command.getDescription())
				.build();

		return createJourneyRequestPort.createJourneyRequest(journeyRequest, username, locale);
	}

	private void checkUserMobileNumberIsVerified(String username) {
		Optional<UserAccount> userAccountOptional = null;
		if (username.contains("@")) {
			userAccountOptional = loadUserAccountPort.loadUserAccountByEmail(username);
		} else {
			String[] mobileNumber = username.split("_");
			userAccountOptional = loadUserAccountPort.loadUserAccountByIccAndMobileNumber(mobileNumber[0],
					mobileNumber[1]);
		}

		if (userAccountOptional.isEmpty()) {
			throw new UserAccountNotFoundException("User " + username + " does not exist.");
		}

		UserAccount userAccount = userAccountOptional.get();

		if (!userAccount.getIsValidatedMobileNumber()) {
			throw new UserMobileNumberValidationException("User " + username + " mobile number is not yet validated.");
		}

	}
}
