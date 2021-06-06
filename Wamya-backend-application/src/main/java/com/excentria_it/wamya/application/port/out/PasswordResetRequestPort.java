package com.excentria_it.wamya.application.port.out;

import java.time.Instant;
import java.util.UUID;

public interface PasswordResetRequestPort {

	UUID registerRequest(Long userId, Instant expityTimestamp);

	boolean requestExists(String uuid, Long expiry);

	Long getUserAccountOauthId(String uuid);

	void purgeExpired();

	void deleteRequest(String uuid);

}
