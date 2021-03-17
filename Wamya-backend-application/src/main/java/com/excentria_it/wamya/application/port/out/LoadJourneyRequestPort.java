package com.excentria_it.wamya.application.port.out;

import java.util.Optional;

import com.excentria_it.wamya.domain.ClientJourneyRequestDto;
import com.excentria_it.wamya.domain.JourneyRequestInputOutput;

public interface LoadJourneyRequestPort {
	Optional<JourneyRequestInputOutput> loadJourneyRequestById(Long id);

	Optional<ClientJourneyRequestDto> loadJourneyRequestByIdAndClientEmail(Long id, String email);

	Optional<ClientJourneyRequestDto> loadJourneyRequestByIdAndClientMobileNumberAndIcc(Long id, String mobileNumber,
			String icc);

	boolean isExistentAndNotExpiredJourneyRequestByIdAndClientEmail(Long journeyRequestId, String clientUsername);

	boolean isExistentAndNotExpiredJourneyRequestByIdAndClientMobileNumberAndIcc(Long journeyRequestId, String string,
			String string2);
}
