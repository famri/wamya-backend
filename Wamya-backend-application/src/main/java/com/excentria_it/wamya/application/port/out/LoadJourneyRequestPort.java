package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.domain.ClientJourneyRequestDtoOutput;
import com.excentria_it.wamya.domain.JourneyRequestInputOutput;
import com.excentria_it.wamya.domain.JourneyRequestStatusCode;

import java.util.Optional;
import java.util.Set;

public interface LoadJourneyRequestPort {
    Optional<JourneyRequestInputOutput> loadJourneyRequestById(Long id);

    Optional<ClientJourneyRequestDtoOutput> loadJourneyRequestByIdAndClientSubject(Long id, String clientSubject, String locale);

    Set<Long> loadJourneyRequestIdsByStatusCodeAndLimit(JourneyRequestStatusCode statusCode, Integer limit);

    boolean isExistentAndNotExpiredJourneyRequestByIdAndClientSubject(Long journeyRequestId, String clientSubject);


}
