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

import com.excentria_it.wamya.application.port.in.CreateDiscussionUseCase;
import com.excentria_it.wamya.application.port.in.CreateDiscussionUseCase.CreateDiscussionCommand;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.domain.LoadDiscussionsDto;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class CreateDiscussionController {

	private final CreateDiscussionUseCase createDiscussionUseCase;

	@PostMapping(path = "/me/discussions")
	@ResponseStatus(HttpStatus.CREATED)
	public LoadDiscussionsDto createDiscussion(@Valid @RequestBody CreateDiscussionCommand command,
			final @AuthenticationPrincipal JwtAuthenticationToken principal) {

		return createDiscussionUseCase.createDiscussion(command, principal.getName());
	}

}
