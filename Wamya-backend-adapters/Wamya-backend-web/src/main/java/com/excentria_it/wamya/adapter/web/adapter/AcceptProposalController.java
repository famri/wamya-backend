package com.excentria_it.wamya.adapter.web.adapter;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.AcceptProposalUseCase;
import com.excentria_it.wamya.application.port.in.AcceptProposalUseCase.AcceptProposalCommand;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.utils.LocaleUtils;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = "/journey-requests", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AcceptProposalController {

	private final AcceptProposalUseCase acceptProposalUseCase;

	@PatchMapping(path = "/{journeyRequestId}/proposals/{proposalId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void acceptProposal(@PathVariable("journeyRequestId") Long journeyRequestId,
			@PathVariable("proposalId") Long proposalId, @Valid @RequestBody AcceptProposalCommand command,
			final @AuthenticationPrincipal JwtAuthenticationToken principal, Locale locale) {
		Locale supportedLocale = LocaleUtils.getSupporedLocale(locale);
		acceptProposalUseCase.acceptProposal(journeyRequestId, proposalId, principal.getName(),
				supportedLocale.toString());

	}

}
