package com.excentria_it.wamya.application.port.in;

import java.util.Locale;

public interface RequestPasswordResetUseCase {

	void requestPasswordReset(String username, Locale locale);

}
