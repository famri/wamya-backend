package com.excentria_it.wamya.application.port.in;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface UpdateEmailSectionUseCase {

	void updateEmailSection(UpdateEmailSectionCommand command, String name);

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	class UpdateEmailSectionCommand {

		@NotEmpty
		String email;

	}

}
