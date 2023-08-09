package com.excentria_it.wamya.adapter.web.adapter;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.adapter.web.utils.ValidationHelper;
import com.excentria_it.wamya.application.port.in.CountMessagesUseCase;
import com.excentria_it.wamya.application.port.in.CountMessagesUseCase.CountMessagesCommand;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.domain.CountMissedMessagesResult;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/me", produces = MediaType.APPLICATION_JSON_VALUE)
public class CountMissedMessagesController {
	
	private final CountMessagesUseCase counMessagesUseCase;
	private final ValidationHelper validationHelper;
	
	@GetMapping("/messages/count")
	@ResponseStatus(HttpStatus.OK)
	public CountMissedMessagesResult countMissedMessages(@RequestParam(name = "read") Boolean read,
			final @AuthenticationPrincipal JwtAuthenticationToken principal) {

		CountMessagesCommand command = CountMessagesCommand.builder().subject(principal.getName())
				.read(read.toString()).build();
		
		validationHelper.validateInput(command);
		
		
		Long count = counMessagesUseCase.countMessages(command);

		return new CountMissedMessagesResult(count);
	}
}
