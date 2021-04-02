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
import com.excentria_it.wamya.application.port.in.LoadMessagesCommandUseCase;
import com.excentria_it.wamya.application.port.in.LoadMessagesCommandUseCase.LoadMessagesCommand;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.domain.LoadMessagesResult;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = "/users/me", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoadDiscussionMessagesController {

	private final LoadMessagesCommandUseCase loadMessagesCommandUseCase;
	private final ValidationHelper validationHelper;

	@GetMapping(path = "/discussions/{discussionId}/messages")
	@ResponseStatus(HttpStatus.OK)
	public LoadMessagesResult loadDiscussions(@PathVariable(name = "discussionId") Long discussionId,
			@RequestParam(name = "page", defaultValue = "0") Integer pageNumber,
			@RequestParam(name = "size", defaultValue = "25") Integer pageSize,
			final @AuthenticationPrincipal JwtAuthenticationToken principal) {

		SortCriterion sortingCriterion = new SortCriterion("date-time", "desc");

		LoadMessagesCommand command = LoadMessagesCommand.builder().username(principal.getName())
				.discussionId(discussionId).pageNumber(pageNumber).pageSize(pageSize).sortingCriterion(sortingCriterion)
				.build();

		validationHelper.validateInput(command);

		return loadMessagesCommandUseCase.loadMessages(command);

	}
}
