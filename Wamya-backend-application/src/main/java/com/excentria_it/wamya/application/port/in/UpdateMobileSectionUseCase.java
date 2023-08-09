package com.excentria_it.wamya.application.port.in;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface UpdateMobileSectionUseCase {

	void updateMobileSection(@Valid UpdateMobileSectionCommand command, String name);

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	class UpdateMobileSectionCommand {

		@NotNull
		Long iccId;

		@NotEmpty
		String mobileNumber;

	}

}
