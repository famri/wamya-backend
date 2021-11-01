package com.excentria_it.wamya.adapter.web.adapter;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.RequestPasswordResetUseCase;
import com.excentria_it.wamya.application.port.in.RequestPasswordResetUseCase.RequestPasswordResetCommand;
import com.excentria_it.wamya.application.port.in.ResetPasswordUseCase;
import com.excentria_it.wamya.application.port.in.ResetPasswordUseCase.ResetPasswordCommand;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.exception.LinkExpiredException;
import com.excentria_it.wamya.common.utils.LocaleUtils;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
public class PasswordResetRestController {

	private final RequestPasswordResetUseCase requestPasswordResetUseCase;

	private final ResetPasswordUseCase resetPasswordUseCase;

	@PostMapping(path = "/do-request-password-reset", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void requestPasswordReset(@Valid @RequestBody RequestPasswordResetCommand command, Locale locale) {

		Locale supportedLocale = LocaleUtils.getSupporedLocale(locale);
		requestPasswordResetUseCase.requestPasswordReset(command.getUsername(), supportedLocale);

	}

	@PostMapping(path = "/password-reset", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void resetPassword(@Valid @RequestBody ResetPasswordCommand command) {

		if (!resetPasswordUseCase.checkRequest(command.getUuid(), command.getExpiry())) {
			throw new LinkExpiredException("Password reset link has expired.");
		}

		resetPasswordUseCase.resetPassword(command.getUuid(), command.getPassword());

	}
}
