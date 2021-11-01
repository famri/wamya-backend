package com.excentria_it.wamya.adapter.web.adapter;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.SendMessageUseCase;
import com.excentria_it.wamya.application.port.in.SendMessageUseCase.SendMessageCommand;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.domain.LoadDiscussionsDto.MessageDto;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/me/discussions", produces = MediaType.APPLICATION_JSON_VALUE)
public class SendMessageController {

	private final SendMessageUseCase sendMessageUseCase;

	@PostMapping(path = "/{discussionId}/messages", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public MessageDto sendMessage(@Valid @RequestBody SendMessageCommand command,
			@PathVariable("discussionId") Long discussionId,

			final @AuthenticationPrincipal JwtAuthenticationToken principal) {

		return sendMessageUseCase.sendMessage(command, discussionId, principal.getName());

	}
}
