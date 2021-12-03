package com.excentria_it.wamya.application.port.in;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface SendTransporterRatingUseCase {

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	class SendTransporterRatingCommand {
		@NotNull
		Long uid;

		@NotEmpty
		String hash;

		String comment;

		@NotNull
		Integer rating;

	}

	void saveTransporterRating(SendTransporterRatingCommand command, String locale);

}
