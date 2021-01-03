package com.excentria_it.wamya.application.port.out;

import java.util.Optional;

import com.excentria_it.wamya.domain.ClientJourneyRequestDto;
import com.excentria_it.wamya.domain.CreateJourneyRequestDto;

public interface LoadJourneyRequestPort {
	Optional<CreateJourneyRequestDto> loadJourneyRequestById(Long id);

	Optional<ClientJourneyRequestDto> loadJourneyRequestByIdAndClientEmail(Long id, String email);

	Optional<ClientJourneyRequestDto> loadJourneyRequestByIdAndClientMobileNumberAndIcc(Long id, String mobileNumber,
			String icc);
}
