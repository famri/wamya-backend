package com.excentria_it.wamya.adapter.web.adapter;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.SaveUserPreferenceUseCase;
import com.excentria_it.wamya.application.port.in.SaveUserPreferenceUseCase.SaveUserPreferenceCommand;
import com.excentria_it.wamya.common.annotation.WebAdapter;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/user-preferences", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserPreferencesController {

	private final SaveUserPreferenceUseCase saveUserPreferenceUseCase;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void acceptProposal(@Valid @RequestBody SaveUserPreferenceCommand command,
			final @AuthenticationPrincipal JwtAuthenticationToken principal) {

		saveUserPreferenceUseCase.saveUserPreference(command.getKey(), command.getValue(), principal.getName());

	}
}
