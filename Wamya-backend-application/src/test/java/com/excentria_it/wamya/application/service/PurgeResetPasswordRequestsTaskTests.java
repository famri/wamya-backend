package com.excentria_it.wamya.application.service;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.out.PasswordResetRequestPort;
import com.excentria_it.wamya.application.service.PurgeResetPasswordRequestsTask;

@ExtendWith(MockitoExtension.class)
public class PurgeResetPasswordRequestsTaskTests {
	@Mock
	private PasswordResetRequestPort passwordResetRequestPort;

	@InjectMocks
	private PurgeResetPasswordRequestsTask purgeResetPasswordRequestsTask;

	@Test
	void testPurgeExpiredPasswordResetRequests() {
		purgeResetPasswordRequestsTask.purgeExpiredPasswordResetRequests();
		then(passwordResetRequestPort).should(times(1)).purgeExpired();
	}
}
