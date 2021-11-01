package com.excentria_it.wamya.adapter.web.adapter;

import java.util.Locale;

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

import com.excentria_it.wamya.application.port.in.UpdateJourneyRequestStatusUseCase;
import com.excentria_it.wamya.application.port.in.UpdateJourneyRequestStatusUseCase.UpdateJourneyRequestStatusCommand;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.utils.LocaleUtils;
import com.excentria_it.wamya.domain.JourneyRequestStatusCode;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/journey-requests", produces = MediaType.APPLICATION_JSON_VALUE)
public class UpdateJourneyRequestStatusController {

	private final UpdateJourneyRequestStatusUseCase updatelJourneyRequestStatusUseCase;

	@PatchMapping(path = "/{id}/status", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateJourneyRequest(@PathVariable(name = "id") Long journeyRequestId,
			@Valid @RequestBody UpdateJourneyRequestStatusCommand command,
			final @AuthenticationPrincipal JwtAuthenticationToken principal, Locale locale) {

		updatelJourneyRequestStatusUseCase.updateStatus(journeyRequestId, principal.getName(),
				JourneyRequestStatusCode.valueOf(command.getStatus().toUpperCase()),
				LocaleUtils.getSupporedLocale(locale).toString());

	}

}
