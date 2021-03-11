package com.excentria_it.wamya.application.port.out;

public interface SaveUserPreferencePort {

	void saveUserPreference(String key, String value, String username);

}
