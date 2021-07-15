package com.excentria_it.wamya.application.port.in;

import javax.validation.constraints.NotEmpty;

import com.excentria_it.wamya.common.annotation.Among;
import com.excentria_it.wamya.domain.JourneyRequestStatusCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface UpdateJourneyRequestStatusUseCase {
	void updateStatus(Long journeyRequestId, String username, JourneyRequestStatusCode status, String locale);

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	class UpdateJourneyRequestStatusCommand {
		@NotEmpty
		@Among(value = { "canceled" })
		private String status;
	}
}
