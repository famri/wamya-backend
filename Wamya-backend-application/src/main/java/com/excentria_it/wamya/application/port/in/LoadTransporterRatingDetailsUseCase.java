package com.excentria_it.wamya.application.port.in;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.excentria_it.wamya.domain.TransporterRatingRequestRecordDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface LoadTransporterRatingDetailsUseCase {

	TransporterRatingRequestRecordDto loadTransporterRatingDetails(LoadTransporterRatingRequestCommand command,
			String locale);

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	class LoadTransporterRatingRequestCommand {

		@NotEmpty
		private String hash;

		@NotNull
		private Long userId;

	}
}
