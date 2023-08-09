package com.excentria_it.wamya.application.port.in;

import javax.validation.constraints.NotEmpty;

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
		@NotEmpty
		@Among(value = { "timezone", "locale" })
		private String key;

		@NotEmpty
		private String value;
	}

}
