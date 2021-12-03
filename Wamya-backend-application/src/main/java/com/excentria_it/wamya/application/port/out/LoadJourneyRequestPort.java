package com.excentria_it.wamya.application.port.out;

import java.util.Optional;
import java.util.Set;

import com.excentria_it.wamya.domain.ClientJourneyRequestDtoOutput;
import com.excentria_it.wamya.domain.JourneyRequestInputOutput;
import com.excentria_it.wamya.domain.JourneyRequestStatusCode;

public interface LoadJourneyRequestPort {
	Optional<JourneyRequestInputOutput> loadJourneyRequestById(Long id);

	Optional<ClientJourneyRequestDtoOutput> loadJourneyRequestByIdAndClientEmail(Long id, String email, String locale);

	Set<Long> loadJourneyRequestIdsByStatusCodeAndLimit(JourneyRequestStatusCode statusCode, Integer limit);

	boolean isExistentAndNotExpiredJourneyRequestByIdAndClientEmail(Long journeyRequestId, String clientUsername);

	boolean isExistentAndNotExpiredJourneyRequestByIdAndClientMobileNumberAndIcc(Long journeyRequestId, String string,
			String string2);
}
