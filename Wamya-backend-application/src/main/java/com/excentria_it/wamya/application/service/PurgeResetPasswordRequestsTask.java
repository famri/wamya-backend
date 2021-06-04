package com.excentria_it.wamya.application.service;

import javax.transaction.Transactional;

import org.springframework.scheduling.annotation.Scheduled;

import com.excentria_it.wamya.application.port.out.PasswordResetRequestPort;
import com.excentria_it.wamya.common.annotation.UseCase;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class PurgeResetPasswordRequestsTask {
	private final PasswordResetRequestPort passwordResetRequestPort;

	@Scheduled(cron = "${app.password-reset.purge-cron-expression}")
	public void purgeExpiredPasswordResetRequests() {

		passwordResetRequestPort.purgeExpired();
	}
}
