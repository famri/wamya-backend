package com.excentria_it.wamya.adapter.web.adapter;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.ValidateMobileValidationCodeUseCase;
import com.excentria_it.wamya.application.port.in.ValidateMobileValidationCodeUseCase.ValidateMobileValidationCodeCommand;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.domain.CodeValidationResult;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = "/validation-codes", produces = MediaType.APPLICATION_JSON_VALUE)
public class ValidateMobileValidationCodeController {

	private final ValidateMobileValidationCodeUseCase validateMobileValidationCodeUseCase;

	@PostMapping(path = "/sms/validate", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public CodeValidationResult validateMobileValidationCode(
			@Valid @RequestBody ValidateMobileValidationCodeCommand command,

			final @AuthenticationPrincipal JwtAuthenticationToken principal) {

		boolean isValid = validateMobileValidationCodeUseCase.validateCode(command, principal.getName());

		return new CodeValidationResult(isValid);

	}
}
