package com.excentria_it.wamya.adapter.web.adapter;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.UpdateEmailSectionUseCase;
import com.excentria_it.wamya.application.port.in.UpdateEmailSectionUseCase.UpdateEmailSectionCommand;
import com.excentria_it.wamya.common.annotation.WebAdapter;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/profiles/me", produces = MediaType.APPLICATION_JSON_VALUE)
public class UpdateProfileEmailSectionController {

	private final UpdateEmailSectionUseCase updateEmailSectionUseCase;

	@PatchMapping(path = "/email")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateEmailSection(@Valid @RequestBody UpdateEmailSectionCommand command,
			final @AuthenticationPrincipal JwtAuthenticationToken principal) throws IOException {

		updateEmailSectionUseCase.updateEmailSection(command, principal.getName());

	}

}
