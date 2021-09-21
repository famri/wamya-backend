package com.excentria_it.wamya.adapter.web.adapter;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.adapter.web.utils.ValidationHelper;
import com.excentria_it.wamya.application.port.in.FindDiscussionUseCase;
import com.excentria_it.wamya.application.port.in.FindDiscussionUseCase.FindDiscussionByClientIdAndTransporterIdCommand;
import com.excentria_it.wamya.application.port.in.FindDiscussionUseCase.FindDiscussionByIdCommand;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.domain.LoadDiscussionsDto;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class FindDiscussionController {

	private final FindDiscussionUseCase findDiscussionUseCase;
	private final ValidationHelper validationHelper;

	@GetMapping(path = "/me/discussions", params = { "clientId", "transporterId" })
	@ResponseStatus(HttpStatus.OK)
	public LoadDiscussionsDto loadDiscussionByClientIdAndTransporterId(
			@RequestParam(name = "clientId") Long clientOauthId,
			@RequestParam(name = "transporterId") Long transporterOauthId,
			final @AuthenticationPrincipal JwtAuthenticationToken principal) {

		FindDiscussionByClientIdAndTransporterIdCommand command = FindDiscussionByClientIdAndTransporterIdCommand
				.builder().username(principal.getName()).clientId(clientOauthId).transporterId(transporterOauthId)
				.build();

		validationHelper.validateInput(command);

		return findDiscussionUseCase.findDiscussionByClientIdAndTransporterId(command);
	}

	@GetMapping(path = "/me/discussions/{id}")
	@ResponseStatus(HttpStatus.OK)
	public LoadDiscussionsDto loadDiscussionById(@PathVariable(name = "id") Long discussionId,

			final @AuthenticationPrincipal JwtAuthenticationToken principal) {

		FindDiscussionByIdCommand command = FindDiscussionByIdCommand.builder().username(principal.getName())
				.discussionId(discussionId).build();

		validationHelper.validateInput(command);

		return findDiscussionUseCase.findDiscussionById(command);
	}
}
