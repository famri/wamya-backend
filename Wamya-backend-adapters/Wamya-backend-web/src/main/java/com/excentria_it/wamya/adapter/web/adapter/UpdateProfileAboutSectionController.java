package com.excentria_it.wamya.adapter.web.adapter;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.UpdateAboutSectionUseCase;
import com.excentria_it.wamya.application.port.in.UpdateAboutSectionUseCase.UpdateAboutSectionCommand;
import com.excentria_it.wamya.common.annotation.WebAdapter;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = "/profiles/me", produces = MediaType.APPLICATION_JSON_VALUE)
public class UpdateProfileAboutSectionController {

	private final UpdateAboutSectionUseCase updateAboutSectionUseCase;

	@PatchMapping(path = "/about")
	@ResponseStatus(HttpStatus.OK)
	public void updateAboutSection(@Valid @RequestBody UpdateAboutSectionCommand command,
			final @AuthenticationPrincipal JwtAuthenticationToken principal) throws IOException {

		updateAboutSectionUseCase.updateAboutSection(command, principal.getName());

	}

}
