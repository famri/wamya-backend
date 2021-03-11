package com.excentria_it.wamya.application.port.in;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.excentria_it.wamya.common.annotation.Among;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface SaveUserPreferenceUseCase {
	void saveUserPreference(String key, String value, String name);

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	class SaveUserPreferenceCommand {
		@NotNull
		@Among(value = "timezone")
		private String key;

		@NotNull
		@NotEmpty
		private String value;
	}

}
