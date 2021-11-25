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

import com.excentria_it.wamya.application.port.in.UpdateMobileSectionUseCase;
import com.excentria_it.wamya.application.port.in.UpdateMobileSectionUseCase.UpdateMobileSectionCommand;
import com.excentria_it.wamya.common.annotation.WebAdapter;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/profiles/me", produces = MediaType.APPLICATION_JSON_VALUE)
public class UpdateProfileMobileSectionController {

	private final UpdateMobileSectionUseCase updateMobileSectionUseCase;

	@PatchMapping(path = "/mobile")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateMobileSection(@Valid @RequestBody UpdateMobileSectionCommand command,
			final @AuthenticationPrincipal JwtAuthenticationToken principal) throws IOException {

		updateMobileSectionUseCase.updateMobileSection(command, principal.getName());

	}
}
