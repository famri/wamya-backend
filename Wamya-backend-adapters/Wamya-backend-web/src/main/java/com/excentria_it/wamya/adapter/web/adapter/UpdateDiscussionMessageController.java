
package com.excentria_it.wamya.adapter.web.adapter;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.UpdateMessageReadStatusUseCase;
import com.excentria_it.wamya.application.port.in.UpdateMessageReadStatusUseCase.UpdateMessageReadStatusCommand;
import com.excentria_it.wamya.common.annotation.WebAdapter;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/me", produces = MediaType.APPLICATION_JSON_VALUE)
public class UpdateDiscussionMessageController {

	private final UpdateMessageReadStatusUseCase updateDiscussionMessageReadStatusUseCase;

	@PatchMapping(path = "/discussions/{discussionId}/messages/{messageId}")
	@ResponseStatus(HttpStatus.OK)
	public void updateDiscussionMessageReadStatus(@PathVariable(name = "discussionId") Long discussionId,
			@PathVariable(name = "messageId") Long messageId,
			@Valid @RequestBody UpdateMessageReadStatusCommand command,
			final @AuthenticationPrincipal JwtAuthenticationToken principal) {

		updateDiscussionMessageReadStatusUseCase.updateMessageReadStatus(discussionId, messageId, principal.getName(),
				command);

	}
}
