package com.excentria_it.wamya.application.port.in;

import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface UpdateAboutSectionUseCase {

	void updateAboutSection(@Valid UpdateAboutSectionCommand command, String username);

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	class UpdateAboutSectionCommand {

		@NotNull
		Long genderId;

		@NotEmpty
		String firstname;

		@NotEmpty
		String lastname;

		@NotNull
		LocalDate dateOfBirth;

		String minibio;

	}

}
